package com.github.zzf.learn.app.rpc.http.provider.micrometer;

import com.github.zzf.learn.app.utils.MetricUtil;
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

    @GetMapping("/nanoTimer")
    public void nanoTimer() {
        MetricUtil.nanoTime("ActuatorAutoController.nanoTimer", ThreadLocalRandom.current().nextInt(1000));
    }

}
