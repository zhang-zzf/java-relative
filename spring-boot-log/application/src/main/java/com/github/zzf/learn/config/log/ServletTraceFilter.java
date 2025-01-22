package com.github.zzf.learn.config.log;


import static com.github.zzf.learn.config.log.Tracer.X_TRACE_ID;
import static com.github.zzf.learn.config.log.Tracer.X_TRACE_LOG_ENABLED;

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
public class ServletTraceFilter implements Filter {
    final Tracer tracer = Tracer.INSTANCE;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest &&
            response instanceof HttpServletResponse httpResponse) {
            // set trace id
            httpResponse.addHeader(X_TRACE_ID, tracer.traceId());
            log.info("http -> {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());
            // trace log flag
            String traceEnabled = httpRequest.getHeader(X_TRACE_LOG_ENABLED);
            if (Boolean.parseBoolean(traceEnabled)) {
                tracer.enableTraceLog();
            }
            try {
                chain.doFilter(request, response);
            } finally {
                // remove trace id
                tracer.removeTraceId();
                tracer.disableTraceLog();
            }
        }
        else {
            chain.doFilter(request, response);
        }
    }
}
