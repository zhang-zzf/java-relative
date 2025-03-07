package com.github.zzf.learn.app.config.actuator;

import static java.util.Collections.emptyList;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.BaseUnits;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.netty.util.internal.PlatformDependent;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-10-10
 */
public class NettyNoCleanerMemoryMetrics implements MeterBinder {

    private final Iterable<Tag> tags;

    public NettyNoCleanerMemoryMetrics() {
        this(emptyList());
    }

    public NettyNoCleanerMemoryMetrics(Iterable<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        Iterable<Tag> tagsWithId = Tags.concat(tags, "id", "netty.NoCleaner");
        Gauge.builder("jvm.buffer.memory.used", PlatformDependent.class, (t) -> PlatformDependent.usedDirectMemory())
            .tags(tagsWithId)
            .baseUnit(BaseUnits.BYTES)
            .register(registry);
    }
}
