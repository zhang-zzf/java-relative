package com.github.learn.spring_async.infra.spring.async;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * <p>继承是为了获得行为</p>
 *
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@RequiredArgsConstructor
@Slf4j
public class ThreadPoolTaskExecutorWrapper extends ThreadPoolTaskExecutor {

    /**
     * 方法增强
     */
    @Override
    public Future<?> submit(Runnable task) {
        Runnable wrappedTask = () -> {
            log.info("before task.run()");
            task.run();
            log.info("after task.run()");
        };
        return super.submit(wrappedTask);
    }

    /**
     * 方法增强
     */
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        Callable<T> wrappedTask = () -> {
            log.info("before task.call()");
            final T ret = task.call();
            log.info("after task.call()");
            return ret;
        };
        return super.submit(wrappedTask);
    }

}
