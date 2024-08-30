package com.feng.learn.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * 测试线程池的关闭
 * <p>{@link ExecutorService#shutdown()} 等待所有已提交的任务执行完成后停止，不会中断正在执行任务的线程</p>
 * <p>{@link ExecutorService#shutdownNow()} 中断正在执行任务的线程，返回还未执行的任务</p>
 */
public class ThreadPoolShutdownTest {


    /**
     * {@link ExecutorService#shutdown()} 不会中断正在执行任务的线程
     * <p>1. 不能提交新的task</p>
     * <p>2. 等待已经提交的task执行完成</p>
     * <p>3. 关闭线程池</p>
     */
    @Test
    public void testShutdown() throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(new LoopTask());
        Thread.sleep(3000);

        service.shutdown();
        while (!service.awaitTermination(1, TimeUnit.SECONDS)) {

        }
    }

    /**
     * {@link ExecutorService#shutdownNow()} 会中断正在执行任务的线程
     * <p>1. 中断线程通知所有在执行任务的线程</p>
     * <p>2. 返回所有在等待队列中还未执行的任务</p>
     *
     * <p>正在执行任务的线程会不会停止依赖与提交的loopTask是否响应中断</p>
     */
    @Test
    public void testShutdownNow() throws InterruptedException {
        ExecutorService service = Executors.newScheduledThreadPool(1);
        service.submit(new LoopTask());
        Thread.sleep(3000);

        service.shutdownNow();
        while (!service.awaitTermination(1, TimeUnit.SECONDS)) {
        }
    }
}
