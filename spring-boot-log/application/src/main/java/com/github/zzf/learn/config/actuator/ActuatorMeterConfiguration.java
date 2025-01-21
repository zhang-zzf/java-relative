package com.github.zzf.learn.config.actuator;

import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics;
import io.github.mweirauch.micrometer.jvm.extras.ProcessThreadMetrics;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.netty4.NettyAllocatorMetrics;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActuatorMeterConfiguration {

    @Bean
    public MeterBinder processMemoryMetrics() {
        return new ProcessMemoryMetrics();
    }

    @Bean
    public MeterBinder processThreadMetrics() {
        return new ProcessThreadMetrics();
    }

    /**
     * netty NoCleaner 指标
     */
    @Bean
    public MeterBinder nettyNoCleanerMemoryMetrics() {
        return new NettyNoCleanerMemoryMetrics();
    }

    /**
     * netty UnpooledByteBufAllocator 指标
     */
    @Bean
    public MeterBinder unpooledByteBufAllocatorMetrics() {
        return new NettyAllocatorMetrics(UnpooledByteBufAllocator.DEFAULT);
    }

    /**
     * netty PooledByteBufAllocator 指标
     */
    @Bean
    public MeterBinder pooledByteBufAllocatorMetrics() {
        return new NettyAllocatorMetrics(PooledByteBufAllocator.DEFAULT);
    }

}
