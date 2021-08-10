package com.github.learn.spring_async.infra.spring.async.task_decorator;

import com.github.learn.spring_async.infra.context.Context;
import lombok.extern.slf4j.Slf4j;
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
        final Runnable decorate = taskDecorator.decorate(runnable);
        final Long userId = Context.USER_ID.get();
        log.info("ContextPropagateDecorator => {}, {}", Thread.currentThread().getName(), userId);
        Runnable task = () -> {
            Context.USER_ID.set(userId);
            decorate.run();
            Context.USER_ID.remove();
        };
        return task;
    }
}
