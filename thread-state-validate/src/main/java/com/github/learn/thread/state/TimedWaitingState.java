package com.github.learn.thread.state;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2022/05/10
 */
@Slf4j
public class TimedWaitingState {

    private final Object monitor = new Object();

    @SneakyThrows
    public void validate() {
        Runnable task = () -> {
            synchronized (monitor) {
                while (!Thread.interrupted()) {
                    try {
                        monitor.wait(Integer.MAX_VALUE);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };
        // noinspection AlibabaAvoidManuallyCreateThread
        final Thread t = new Thread(task, "thread-state-TIMED_WAITING");
        t.start();
        Thread.sleep(1000);
        log.info("Thread: {} -> state: {}", t, t.getState());
    }

}
