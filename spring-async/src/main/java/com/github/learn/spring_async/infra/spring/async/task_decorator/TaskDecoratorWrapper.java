package com.github.learn.spring_async.infra.spring.async.task_decorator;

import lombok.RequiredArgsConstructor;
import org.springframework.core.task.TaskDecorator;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@RequiredArgsConstructor
public abstract class TaskDecoratorWrapper implements TaskDecorator {

    protected final TaskDecorator taskDecorator;

}
