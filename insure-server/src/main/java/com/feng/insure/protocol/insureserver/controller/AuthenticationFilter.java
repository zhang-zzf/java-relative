package com.feng.insure.protocol.insureserver.controller;

import com.alibaba.fastjson.JSON;
import com.feng.insure.protocol.insureserver.controller.model.ErrorData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/12/7
 */
@Slf4j
@RequiredArgsConstructor
@WebFilter
@Component
public class AuthenticationFilter implements Filter {

    public static final String X_REQUEST_TIMESTAMP = "x-request-timestamp";
    public static final String X_CLIENT_PUBLIC_KEY = "x-client-public-key";
    public static final String X_REQUEST_SIGN = "x-request-sign";

    private final CipherUtil cipherUtil = CipherUtil.getInstance();

    private final AuthenticationService authenticationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        final String publicKey = httpServletRequest.getHeader(X_CLIENT_PUBLIC_KEY);
        // 用户名认证
        if (!authenticationService.authenticate(publicKey)) {
            // 认证失败
            final ErrorData resData = new ErrorData()
                    .setPath(httpServletRequest.getServletPath()).setStatus("401")
                    .setCode("AUTHENTICATE_FAILED").setMessage("客户端公钥未注册");
            httpServletResponse.setStatus(401);
            httpServletResponse.setContentType("application/json; charset=UTF-8");
            httpServletResponse.getWriter().print(JSON.toJSONString(resData));
            // return 401
            return;
        }
        // 签名验证
        final ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        if (!verifyRequest(requestWrapper)) {
            // 签名错误
            final ErrorData resData = new ErrorData()
                    .setPath(httpServletRequest.getServletPath()).setStatus("400")
                    .setCode("INVALID_ARGUMENT").setMessage("SIGNATURE_VERIFY_FAILED 请求签名验证失败");
            httpServletResponse.setStatus(400);
            httpServletResponse.setContentType("application/json; charset=UTF-8");
            httpServletResponse.getWriter().print(JSON.toJSONString(resData));
            // return 400
            return;
        }
        chain.doFilter(requestWrapper, response);
    }

    @SneakyThrows
    private boolean verifyRequest(HttpServletRequest httpServletRequest) {
        final String publicKey = httpServletRequest.getHeader(X_CLIENT_PUBLIC_KEY);
        final String signature = httpServletRequest.getHeader(X_REQUEST_SIGN);
        StringBuilder signBody = new StringBuilder();
        signBody.append(X_REQUEST_TIMESTAMP).append(": ").append(httpServletRequest.getHeader(X_REQUEST_TIMESTAMP)).append("\r\n");
        signBody.append(X_CLIENT_PUBLIC_KEY).append(": ").append(publicKey).append("\r\n");
        signBody.append("\r\n");
        // 签名 body 部分
        final String method = httpServletRequest.getMethod().toUpperCase();
        String requestBody = "";
        if (HttpMethod.GET.name().equals(method)) {
            // get
            final Optional<CharSequence> sortedQueryParam = httpServletRequest.getParameterMap().entrySet().stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(e -> CollectionFormat.CSV.join(e.getKey(), Arrays.asList(e.getValue()), StandardCharsets.UTF_8))
                    .reduce((p1, p2) -> p1 + "&" + p2);
            if (sortedQueryParam.isPresent()) {
                requestBody = sortedQueryParam.get().toString();
            }
        } else {
            StringBuilder body = new StringBuilder();
            String line;
            try (BufferedReader reader = httpServletRequest.getReader()) {
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
            }
            requestBody = body.toString();
        }
        signBody.append(requestBody);
        log.info("request to verify: publicKey={}, signature={}, req={}", publicKey, signature, requestBody);
        final boolean verify = cipherUtil.md5WithRSAVerify(
                signBody.toString().getBytes(StandardCharsets.UTF_8),
                cipherUtil.base64Decode(signature),
                publicKey);
        if (!verify) {
            log.error("request verify failed: publicKey={}, signature={}, req={}", publicKey, signature, requestBody);
        }
        return verify;
    }


    public static class ContentCachingRequestWrapper extends HttpServletRequestWrapper {

        private byte[] body;

        private BufferedReader reader;

        public ContentCachingRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            body = IOUtils.toByteArray(request.getInputStream());
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new RequestCachingInputStream(body);
        }

        @Override
        public BufferedReader getReader() throws IOException {
            if (reader == null) {
                reader = new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
            }
            return reader;
        }

        private static class RequestCachingInputStream extends ServletInputStream {

            private final ByteArrayInputStream inputStream;

            public RequestCachingInputStream(byte[] bytes) {
                inputStream = new ByteArrayInputStream(bytes);
            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return inputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readlistener) {
            }

        }

    }

    public enum CollectionFormat {
        /**
         * Comma separated values, eg foo=bar,baz
         */
        CSV(","),
        /**
         * Space separated values, eg foo=bar baz
         */
        SSV(" "),
        /**
         * Tab separated values, eg foo=bar[tab]baz
         */
        TSV("\t"),
        /**
         * Values separated with the pipe (|) character, eg foo=bar|baz
         */
        PIPES("|"),
        /**
         * Parameter name repeated for each value, eg foo=bar&foo=baz
         */
        // Using null as a special case since there is no single separator character
        EXPLODED(null);

        private final String separator;

        CollectionFormat(String separator) {
            this.separator = separator;
        }

        /**
         * Joins the field and possibly multiple values with the given separator.
         *
         * <p>
         * Calling EXPLODED.join("foo", ["bar"]) will return "foo=bar".
         * </p>
         *
         * <p>
         * Calling CSV.join("foo", ["bar", "baz"]) will return "foo=bar,baz".
         * </p>
         *
         * <p>
         * Null values are treated somewhat specially. With EXPLODED, the field is repeated without any
         * "=" for backwards compatibility. With all other formats, null values are not included in the
         * joined value list.
         * </p>
         *
         * @param field The field name corresponding to these values.
         * @param values A collection of value strings for the given field.
         * @param charset to encode the sequence
         * @return The formatted char sequence of the field and joined values. If the value collection is
         * empty, an empty char sequence will be returned.
         */
        public CharSequence join(String field, Collection<String> values, Charset charset) {
            StringBuilder builder = new StringBuilder();
            int valueCount = 0;
            for (String value : values) {
                if (separator == null) {
                    // exploded
                    builder.append(valueCount++ == 0 ? "" : "&");
                    builder.append(UriUtils.encode(field, charset));
                    if (value != null) {
                        builder.append('=');
                        builder.append(value);
                    }
                } else {
                    // delimited with a separator character
                    if (builder.length() == 0) {
                        builder.append(UriUtils.encode(field, charset));
                    }
                    if (value == null) {
                        continue;
                    }
                    builder.append(valueCount++ == 0 ? "=" : UriUtils.encode(separator, charset));
                    builder.append(UriUtils.encode(value, charset));
                }
            }
            return builder;
        }
    }


}
