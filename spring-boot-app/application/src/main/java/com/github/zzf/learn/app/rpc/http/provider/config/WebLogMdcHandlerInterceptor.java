package com.github.zzf.learn.app.rpc.http.provider.config;

import static com.github.zzf.learn.app.config.trace.Tracer.X_TRACE_ID;

import com.github.zzf.learn.app.config.trace.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class WebLogMdcHandlerInterceptor implements HandlerInterceptor {

    final Tracer tracer = Tracer.INSTANCE;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        try {
            response.addHeader(X_TRACE_ID, tracer.traceId());
        } catch (Exception e) {
            log.error("preHandle -> unExpected exception", e);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
        tracer.removeTraceId();
    }
}
