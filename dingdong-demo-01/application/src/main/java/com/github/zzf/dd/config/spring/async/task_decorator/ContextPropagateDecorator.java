package com.github.zzf.dd.config.spring.async.task_decorator;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

/**
 * ThreadLocal context 上下文传递
 *
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@Slf4j
public class ContextPropagateDecorator extends TaskDecoratorWrapper {

    public ContextPropagateDecorator(TaskDecorator taskDecorator) {
        super(taskDecorator);
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        final Runnable decorate = super.taskDecorator.decorate(runnable);
        // MDC 上下文传递
        final Map<String, String> mdcContextMap = MDC.getCopyOfContextMap();
        return () -> {
            MDC.setContextMap(mdcContextMap);
            try {
                decorate.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
