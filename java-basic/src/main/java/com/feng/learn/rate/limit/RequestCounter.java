package com.feng.learn.rate.limit;

import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.concurrent.ThreadSafe;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/24
 */
@ThreadSafe
public class RequestCounter {

    private final int periodInMillis;
    private final AtomicLong timestamp = new AtomicLong(System.currentTimeMillis());
    private final AtomicLong counter = new AtomicLong(0L);

    public RequestCounter(int periodInMillis) {
        this.periodInMillis = periodInMillis;
    }

    /**
     * add delta to the counter
     *
     * @param delta increment count of request
     * @return value of the counter after add the increment
     */
    public long add(int delta) {
        final long oldTimestamp = timestamp.get();
        final long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - oldTimestamp >= periodInMillis) {
            if (timestamp.compareAndSet(oldTimestamp, currentTimeMillis)) {
                // rest the counter
                counter.set(0);
            }
        }
        return counter.addAndGet(delta);
    }

}
