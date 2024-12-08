package com.feng.learn.jdk8.lambda;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-08
 */
@Slf4j
public class ExecutorTest {

    /**
     * <pre>
     *     总结：
     *         方法引用只是创建 lambda 的返回值
     *         1.  java.util.concurrent.ExecutorService.submit(java.util.concurrent.Callable<T>)
     *         1.        Future<Task> taskFuture = executor.submit(Task::new);
     *         1.        Future<Runnable> taskFactoryFuture2 = executor.submit(TaskFactory::task);
     * </pre>
     *
     */
    @Test
    void givenExecutor_whenSubmitLambda_then() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //
        executor.submit(() -> {
            // 执行下此段代码
            Runnable task = TaskFactory.task();
            // the result was ignored
        });
        // java.util.concurrent.ExecutorService.submit(java.util.concurrent.Callable<T>)
        Future<Runnable> taskFactoryFuture = executor.submit(() -> {
            // 执行下此段代码
            return TaskFactory.task();
        });
        // 和 taskFactoryFuture 等效
        Future<Runnable> taskFactoryFuture2 = executor.submit(TaskFactory::task);
        then(taskFactoryFuture.get()).isNotNull();
        then(taskFactoryFuture2.get()).isNotNull();
        //
        Future<?> submit = executor.submit(() -> {
            // 执行了创建 Task 的代码，仅此而已
            new Task();
        });
        //
        // java.util.concurrent.ExecutorService.submit(java.util.concurrent.Callable<T>)
        // watch out: 提交一个任务，任务中创建了一个 Task， 并不会执行 Task。
        Future<Task> taskFuture = executor.submit(Task::new);
        // 和 taskFuture 等效
        Future<Task> taskFuture1 = executor.submit(() -> {
            // 执行了创建 Task 的代码，仅此而已
            return new Task();
        });
        then(taskFuture.get()).isNotNull();
    }


}

@Slf4j
class Task implements Runnable {
    @Override
    public void run() {
        log.info("Task.tun");
    }
}

@Slf4j
class TaskFactory {
    public static Runnable task() {
        log.info("TaskFactory.task");
        return () -> {
            log.info("TaskFactory.task.run");
        };
    }
}
