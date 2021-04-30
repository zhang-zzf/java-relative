package com.feng.learn.interview;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 分析以下代码
 *
 * @author zhanfeng.zhang
 * @date 2020/07/22
 */
public class Monitor {

    @RequiredArgsConstructor
    public static class MonitorKey {
        private final String key;
        private final String desc;
    }

    public static class MonitorValue {
        private final AtomicInteger counter = new AtomicInteger();
        private final AtomicLong totalTime = new AtomicLong();
        // volatile 可见性问题
        private double avgTime;
    }

    private final Map<MonitorKey, MonitorValue> monitors = new ConcurrentHashMap<>();

    public void visit(String url, String desc, long timeCost) {
        MonitorKey key = new MonitorKey(url, desc);
        // MonitorKey does not override hashCode() and equals() will lead to memory explode
        MonitorValue value = monitors.get(key);
        if (value == null) {
            value = new MonitorValue();
            // concurrency problem
            monitors.put(key, value);
            monitors.putIfAbsent(key, value);
        }
        int afterCount = value.counter.addAndGet(1);
        long afterTime = value.totalTime.addAndGet(timeCost);
        value.avgTime = afterTime / afterCount;
    }

}
