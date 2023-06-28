package com.github.learn;

import lombok.SneakyThrows;

/**
 * @author zhanfeng.zhang
 * @date 2022/05/07
 */
public class ThreadRamLocation {

  public void startThread(String[] args) {
    final int threadNum = Integer.parseInt(args[0]);
    final int threadStackDeep = Integer.parseInt(args[1]);
    Runnable task = () -> recursive(0, threadStackDeep);
    for (int i = 0; i < threadNum; i++) {
      //noinspection AlibabaAvoidManuallyCreateThread
      new Thread(task, "thread-" + i).start();
    }

  }

  @SneakyThrows
  public void recursive(int curDeep, int targetDeep) {
    if (curDeep < targetDeep) {
      recursive(curDeep + 1, targetDeep);
    } else {
      Thread.currentThread().join();
    }
  }

}
