package com.github.zzf.dd.config.log;


import com.github.zzf.dd.common.annotation.Idempotent;
import com.github.zzf.dd.common.id.generator.TimeBasedSnowFlake;
import org.slf4j.MDC;

public final class LogTracer {
    public static final LogTracer INSTANCE = new LogTracer();
    public static final String X_TRACE_ID = "x-trace-id";

    private LogTracer() {
    }

    @Idempotent
    public String addTraceId() {
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
