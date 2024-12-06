package com.feng.learn.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class ScheduledThreadPoolTest {

    /**
     * <pre>
     *   1. mark start time
     *   1. run task
     *   1. submit next task (next exec time = period-(now-start))
     * </pre>
     */
    @Test
    public void test() throws InterruptedException {
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);

        scheduled.scheduleAtFixedRate(() -> {
            System.out.println(Thread.currentThread().getName() + " start..");
            try {
                Thread.sleep(20 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " end..");
        }, 2, 10, TimeUnit.SECONDS);

        Thread.currentThread().join();
    }

    /**
     * <pre>
     *   1. run task
     *   1. submit next task, exec time = delay
     * </pre>
     */
    @Test
    public void test2() throws InterruptedException {
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);

        scheduled.scheduleWithFixedDelay(() -> {
            System.out.println(
                Thread.currentThread().getName() + " start at: " + System
                    .currentTimeMillis());
            try {
                Thread.sleep(20 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " end at: " + System
                .currentTimeMillis());
        }, 2, 10, TimeUnit.SECONDS);

        Thread.currentThread().join();
    }
}

