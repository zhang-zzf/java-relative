package com.github.zzf.actuator.common.spring.async.task_decorator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskDecorator;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@Slf4j
public class TimedDecorator extends TaskDecoratorWrapper {

    public TimedDecorator(TaskDecorator taskDecorator) {
        super(taskDecorator);
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        final Runnable decorate = taskDecorator.decorate(runnable);
        return () -> {
            final long start = System.currentTimeMillis();
            decorate.run();
            log.info("{} task executed in {}ms", Thread.currentThread().getName(),
                System.currentTimeMillis() - start);
        };
    }
}
