package com.github.learn.thread.state;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2022/05/10
 */
@Slf4j
public class RunnableState {

  @SneakyThrows
  public void validate() {
    //noinspection AlibabaAvoidManuallyCreateThread
    final Thread t = new Thread(() -> {
      // 模拟 CPU 计算
      while (true) {
      }
    }, "thread-state-RUNNABLE");
    t.start();
    log.info("Thread: {} -> state: {}", t, t.getState());
    Thread.sleep(1000);
    log.info("Thread: {} -> state: {}", t, t.getState());
  }

}
