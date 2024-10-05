package com.github.learn.ratelimiter;

import java.util.concurrent.atomic.AtomicLong;

public class SlidingWindow {
  private final int period;
  private final int precision;
  private final int[] slices;
  private final AtomicLong oldestTime;

  // 30s 内 7 个窗口
  public SlidingWindow(int period, int precision) {
    this.period = period;
    this.precision = precision;
    this.slices = new int[precision];
    oldestTime = new AtomicLong(System.currentTimeMillis());
  }
}
