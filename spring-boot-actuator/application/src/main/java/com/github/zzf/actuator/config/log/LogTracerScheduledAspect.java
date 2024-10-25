package com.github.zzf.actuator.config.log;

import com.github.zzf.actuator.common.log.LogTracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Scheduled;

@EnableAspectJAutoProxy
@Configuration
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LogTracerScheduledAspect {

    final LogTracer logTracer = LogTracer.INSTANCE;

    /**
     * 调度任务
     */
    @Around("execution(* *(..)) && @annotation(annotation)")
    public Object addTraceId(ProceedingJoinPoint pjp, Scheduled annotation) throws Throwable {
        try {
            logTracer.addTraceId();
            return pjp.proceed();
        } finally {
            logTracer.removeTraceId();
        }
    }

}
