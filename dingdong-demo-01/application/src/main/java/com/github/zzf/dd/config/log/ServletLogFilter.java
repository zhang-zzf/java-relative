package com.github.zzf.dd.config.log;



import static com.github.zzf.dd.config.log.LogTracer.X_TRACE_ID;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-10-08
 */
@Slf4j
public class ServletLogFilter implements Filter {
    final LogTracer logTracer = LogTracer.INSTANCE;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        if (request instanceof HttpServletRequest &&
            response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            // set trace id
            httpResponse.addHeader(X_TRACE_ID, logTracer.addTraceId());
            log.info("http -> {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());
            chain.doFilter(request, response);
            // remove trace id
            logTracer.removeTraceId();
        }
        else {
            chain.doFilter(request, response);
        }
    }
}
