package com.github.learn.thread.state;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2022/05/10
 */
@Slf4j
public class BlockedState {

  private final Object monitor = new Object();

  @SneakyThrows
  public void validate() {
    Runnable task = new Runnable() {
      @SneakyThrows
      @Override
      public void run() {
        synchronized (monitor) {
          Thread.sleep(Integer.MAX_VALUE);
        }
      }
    };
    //noinspection AlibabaAvoidManuallyCreateThread
    final Thread thread1 = new Thread(task, "thread-state-sleep-with-lock");
    thread1.start();
    Thread.sleep(1000);
    log.info("Thread: {} -> {}", thread1, thread1.getState());
    //noinspection AlibabaAvoidManuallyCreateThread
    final Thread t2 = new Thread(task, "thread-state-BLOCKED");
    t2.start();
    Thread.sleep(1000);
    log.info("Thread: {} -> {}", t2, t2.getState());
  }

}
