package com.github.zzf.learn.config.trace;


import com.github.zzf.learn.common.annotation.Idempotent;
import com.github.zzf.learn.common.id.generator.TimeBasedSnowFlake;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.MDC;

public final class Tracer {
    public static final Tracer INSTANCE = new Tracer();
    public static final String X_TRACE_ID = "x-trace-id";

    final ThreadLocal<Map<String, Object>> traceContext = ThreadLocal.withInitial(HashMap::new);


    private Tracer() {
    }

    public static final String X_TRACE_LOG_ENABLED = "x-trace-log-enabled";

    public boolean isTraceLogEnabled() {
        return Boolean.TRUE.equals(traceContext.get().get(X_TRACE_LOG_ENABLED));
    }

    public void enableTraceLog() {
        traceContext.get().put(X_TRACE_LOG_ENABLED, Boolean.TRUE);
    }

    public void disableTraceLog() {
        traceContext.get().remove(X_TRACE_LOG_ENABLED);
    }

    @Idempotent
    public String traceId() {
        String traceId = MDC.get(X_TRACE_ID);
        if (traceId != null) {
            return traceId;
        }
        long uuid = TimeBasedSnowFlake.generate();
        MDC.put(X_TRACE_ID, String.valueOf(uuid));
        return MDC.get(X_TRACE_ID);
    }


    public void removeTraceId() {
        MDC.remove(X_TRACE_ID);
    }
}
