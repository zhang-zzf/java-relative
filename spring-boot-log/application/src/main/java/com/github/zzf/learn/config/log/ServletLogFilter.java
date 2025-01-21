package com.github.zzf.learn.config.log;


import static com.github.zzf.learn.common.log.LogTracer.X_TRACE_ID;

import com.github.zzf.learn.common.log.LogTracer;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        if (request instanceof HttpServletRequest httpRequest &&
            response instanceof HttpServletResponse httpResponse) {
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
