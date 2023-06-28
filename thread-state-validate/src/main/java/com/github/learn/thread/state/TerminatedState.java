package com.github.learn.thread.state;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2022/05/10
 */
@Slf4j
public class TerminatedState {

  @SneakyThrows
  public void validate() {
    //noinspection AlibabaAvoidManuallyCreateThread
    final Thread t = new Thread(() -> {
    }, "thread-state-TERNINATED");
    t.start();
    Thread.sleep(1000);
    log.info("Thread: {} -> state: {}", t, t.getState());
  }

}
