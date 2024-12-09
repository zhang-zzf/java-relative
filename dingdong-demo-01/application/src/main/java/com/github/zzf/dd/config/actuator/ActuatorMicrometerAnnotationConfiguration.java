package com.github.zzf.dd.config.actuator;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class ActuatorMicrometerAnnotationConfiguration {

    /**
     * <p>Applying TimedAspect makes @Timed usable on any arbitrary method in an AspectJ proxied instance</p>
     * io.micrometer.core.annotation.@Timed aspect
     */
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

}
