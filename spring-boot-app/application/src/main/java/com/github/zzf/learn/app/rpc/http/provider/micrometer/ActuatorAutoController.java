package com.github.zzf.learn.app.rpc.http.provider.micrometer;

import com.github.zzf.learn.app.utils.MetricUtil;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-04-18
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/micrometer")
public class ActuatorAutoController {

    final MeterRegistry meterRegistry;

    /**
     * 探索 spring actuator meterRegistry 是否绑定到 Metrics.globalRegistry
     */
    @GetMapping("/")
    public void checkMeterRegistry() {
        meterRegistry.forEachMeter(meter -> {
            log.info("meter: {}", meter);
        });
        log.info("meterRegistry: {}", meterRegistry);
        log.info("Metrics.: {}", Metrics.globalRegistry);
        // 会自动把 spring context 中的 registry 绑定到 Metrics.globalRegistry 中
        for (MeterRegistry registry : Metrics.globalRegistry.getRegistries()) {
            if (registry == meterRegistry) {
                log.info("meterRegistry was add to Metrics.globalRegistry");
            }
        }
    }

    /**
     * 自定义打点
     */
    @GetMapping("/nanoTimer")
    public void nanoTimer() {
        MetricUtil.nanoTime("ActuatorAutoController.nanoTimer", ThreadLocalRandom.current().nextInt(1000));
    }

    /**
     * 自定义名字和 percentiles
     */
    @GetMapping("/@timed")
    @Timed(value = "ActuatorAutoController.timed", percentiles = {0.5, 0.9, 0.95, 0.99}, extraTags = {"tag1", "time1"})
    public void timed() {
    }

    /**
     * 自定义名字和 percentiles
     */
    @GetMapping("/@counted")
    @Counted(value = "ActuatorAutoController.counted", extraTags = {"tag1", "count1", "tag2", "count2"})
    public void counted() {
    }

}
