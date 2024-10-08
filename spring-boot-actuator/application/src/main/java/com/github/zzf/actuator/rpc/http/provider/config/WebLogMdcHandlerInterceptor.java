package com.github.zzf.actuator.rpc.http.provider.config;

import static com.github.zzf.actuator.common.log.LogTracer.X_TRACE_ID;

import com.github.zzf.actuator.common.log.LogTracer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class WebLogMdcHandlerInterceptor implements HandlerInterceptor {

    final LogTracer logTracer = LogTracer.INSTANCE;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        try {
            response.addHeader(X_TRACE_ID, logTracer.addTraceId());
            log.info("http -> {} {}", request.getMethod(), request.getRequestURI());
        } catch (Exception e) {
            log.error("preHandle -> unExpected exception", e);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
        logTracer.removeTraceId();
    }
}
