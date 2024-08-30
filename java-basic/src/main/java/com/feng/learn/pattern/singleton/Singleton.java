package com.feng.learn.pattern.singleton;

import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.concurrent.ThreadSafe;

/**
 * @author zhanfeng.zhang
 * @date 2020/5/2
 */
public class Singleton {

  private static Singleton instance;

  private Singleton() {
  }

  /**
   * 延迟初始化
   */
  public static Singleton getInstance() {
    if (null == instance) {
      instance = new Singleton();
    }
    return instance;
  }

}

@ThreadSafe
class Singleton2 {

  @lombok.Getter
  private static final Singleton2 instance = new Singleton2();

  private Singleton2() {
  }

}

@ThreadSafe
class Singleton3 {

  private Singleton3() {
  }

  public static Singleton3 getInstance() {
    return InstanceHolder.instance;
  }

  private static class InstanceHolder {
    private static final Singleton3 instance = new Singleton3();
  }
}

class Singleton5 {

  private static Singleton5 instance;

  private Singleton5() {
  }

  public synchronized static Singleton5 getInstance() {
    if (instance == null) {
      instance = new Singleton5();
    }
    return instance;
  }

}

class Singleton6 {

  private static volatile Singleton6 instance;

  private Singleton6() {
  }

  public static Singleton6 getInstance() {
    if (instance == null) {
      synchronized (Singleton6.class) {
        if (instance == null) {
          instance = new Singleton6();
        }
      }
    }
    return instance;
  }

}

class Singleton4 {

  private static final AtomicReference<Object> instanceHolder = new AtomicReference<Object>();

  private Singleton4() {
  }

  public static Singleton4 getInstance() {
    Object instance = instanceHolder.get();
    if (null == instance) {
      instance = new Singleton4();
      if (!instanceHolder.compareAndSet(null, instance)) {
        instance = instanceHolder.get();
      }
    }
    return (Singleton4) instance;
  }
}
