package com.github.zzf.actuator.common.id.generator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public interface IdGeneratorService {
    default long id() {
        return TimeBasedSnowFlake.generate();
    }

    /**
     * 为 spring context 提供默认 bean
     */
    @Configuration
    @Slf4j
    class IdGeneratorServiceAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean(IdGeneratorService.class)
        public IdGeneratorService idGeneratorService() {
            return new IdGeneratorService() {
                @Override
                public long id() {
                    return IdGeneratorService.super.id();
                }
            };
        }
    }

}
