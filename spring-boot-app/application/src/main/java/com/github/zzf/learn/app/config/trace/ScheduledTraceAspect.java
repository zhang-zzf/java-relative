package com.github.zzf.learn.app.config.trace;

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
public class ScheduledTraceAspect {

    final Tracer tracer = Tracer.INSTANCE;

    /**
     * 调度任务
     */
    @Around("execution(* *(..)) && @annotation(annotation)")
    public Object addTraceId(ProceedingJoinPoint pjp, Scheduled annotation) throws Throwable {
        try {
            tracer.traceId();
            return pjp.proceed();
        } finally {
            tracer.removeTraceId();
        }
    }

}
