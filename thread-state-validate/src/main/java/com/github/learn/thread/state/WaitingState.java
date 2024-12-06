package com.github.learn.thread.state;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2022/05/10
 */
@Slf4j
public class WaitingState {

    private final Object monitor = new Object();

    @SneakyThrows
    public void validate() {
        final Runnable task = () -> {
            synchronized (monitor) {
                while (!Thread.interrupted()) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        // 中断当前线程
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };
        // noinspection AlibabaAvoidManuallyCreateThread
        final Thread t = new Thread(task, "thread-state-WAITING");
        t.start();
        // 主线程 sleep 等待 t 启动
        Thread.sleep(1000);
        log.info("Thread: {} -> state:{}", t, t.getState());
    }

}
