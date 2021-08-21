package com.github.learn.id.generator.domain.model;

import lombok.Data;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 一段 id
 *
 * @author zhanfeng.zhang (zhang.zzf@alibaba-inc.com)
 * @date 2021/8/17
 */
@Data
public class Sequence {

    /**
     * id key
     */
    private final String key;

    /**
     * [startId, endId]
     */
    private final long startId;
    private final long endId;
    /**
     * 步进值
     */
    private final int step;
    private final long createdAt = System.currentTimeMillis();

    /**
     * 下一个可用的id
     */
    private final AtomicLong NEXT_ID;

    public Sequence(String key, long startId, long endId, int step) {
        this.key = key;
        this.startId = startId;
        this.NEXT_ID = new AtomicLong(startId);
        this.endId = endId;
        this.step = step;
    }

    /**
     * 初始化 sequence
     *
     * @param key key
     * @return data
     */
    public static Sequence initSequence(String key) {
        return new Sequence(key, 1, 1, 4);
    }

    /**
     * 序列使用百分比
     *
     * @return [0, 100]
     */
    public long usedPercent() {
        long total = endId - startId;
        total = total == 0 ? 1 : total;
        long percent = 100 * (NEXT_ID.get() - startId) / total;
        return percent;
    }

    /**
     * 获取下一个可用id
     * <p>ThreadSafe</p>
     *
     * @return data
     */
    public Optional<Long> nextId() {
        while (true) {
            long nextId = NEXT_ID.get();
            if (nextId > endId) {
                return Optional.empty();
            }
            if (NEXT_ID.compareAndSet(nextId, nextId + step)) {
                return Optional.of(nextId);
            }
        }
    }

}
