package com.github.learn.fix.window;

import java.util.concurrent.atomic.AtomicLong;
import lombok.Data;

/**
 * @author zhanfeng.zhang
 * @date 2021/07/31
 */
@Data
public class Window {

    private final long windowPeriod;
    private final long qps;
    private AtomicLong counter;
    private volatile long lastUpdatedAt;

    public Window(long windowPeriod, long qps) {
        this.windowPeriod = windowPeriod;
        this.qps = qps;
        this.counter = new AtomicLong(0);
        this.lastUpdatedAt = System.currentTimeMillis();
    }

    /**
     * 限流检测
     * <p>线程安全</p>
     *
     * @param delta 资源数
     * @return true-被限流
     */
    public boolean limit(long delta) {
        final long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastUpdatedAt > windowPeriod) {
            synchronized (this) {
                if (currentTimeMillis - lastUpdatedAt > windowPeriod) {
                    counter = new AtomicLong(0);
                    lastUpdatedAt = currentTimeMillis;
                }
            }
        }
        return counter.addAndGet(delta) > qps;
    }


}
