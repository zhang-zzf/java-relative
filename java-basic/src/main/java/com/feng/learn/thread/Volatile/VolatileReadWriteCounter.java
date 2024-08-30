package com.feng.learn.thread.Volatile;

import com.feng.learn.annotation.GuardedBy;
import com.feng.learn.annotation.ThreadSafe;

@ThreadSafe
public class VolatileReadWriteCounter {

    // Employs a cheap read-write lock trick
    // All mutative operations MUST be done with the 'this' lock held.
    @GuardedBy("this")
    private volatile int counter = 0;

    public int get() {
        return counter;
    }

    // 保证只有一个线程更新volatile值
    public synchronized void increment() {
        counter++;
    }
}
