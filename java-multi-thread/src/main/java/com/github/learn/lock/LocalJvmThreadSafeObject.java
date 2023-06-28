package com.github.learn.lock;

import com.github.learn.lock.annotation.GuardedBy;
import com.github.learn.lock.annotation.ThreadSafe;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/28
 */
@ThreadSafe
public class LocalJvmThreadSafeObject {

  /**
   * 锁保护的是资源
   * <p>通常情况下是 JVM 内的数据如本对象的 data 数据</p>
   * <p>对数据的读写都用锁保护才能说是线程安全的对象</p>
   */
  private final Lock lock = new ReentrantLock();
  @GuardedBy("this.lock")
  private Object data;

  public Object getData() {
    lock.lock();
    try {
      return this.data;
    } finally {
      lock.unlock();
    }
  }

  public LocalJvmThreadSafeObject setData(Object data) {
    lock.lock();
    try {
      this.data = data;
      return this;
    } finally {
      lock.unlock();
    }
  }

}
