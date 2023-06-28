package com.feng.learn;

public class CpuFull {

  public static void main(String[] args) {
    int cpuNum = Runtime.getRuntime().availableProcessors() * 2;
    for (int i = 0; i < cpuNum; i++) {
      new Thread(() -> {
        while (true) {
        }
      }).start();
    }
  }
}
