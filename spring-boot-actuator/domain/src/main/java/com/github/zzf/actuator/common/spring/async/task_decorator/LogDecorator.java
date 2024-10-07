package com.github.zzf.actuator.common.spring.async.task_decorator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskDecorator;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@Slf4j
public class LogDecorator extends TaskDecoratorWrapper {

    public LogDecorator(TaskDecorator taskDecorator) {
        super(taskDecorator);
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        final Runnable decorateTask = taskDecorator.decorate(runnable);
        return () -> {
            final String name = Thread.currentThread().getName();
            log.info("{} start to run task.", name);
            decorateTask.run();
            log.info("{} task finished", name);
        };
    }
}
