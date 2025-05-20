package com.github.zzf.learn.app.common.spring.cache;

import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 内存级别 cache
 */
@Configuration
@EnableCaching
public class SpringCaffeineCacheConfig {

    public static final String CAFFEINE_STATION = "CacheManager/repo/caffeine";
    public static final String CAFFEINE_5_MINUTES = "ttl5Minutes";

    /**
     * station 专用 CacheManager
     */
    @Bean(CAFFEINE_STATION)
    @Primary // 存在多个 CacheManager 时必须制定一个默认的 CacheManager
    public CacheManager cacheForStationOnly() {
        // 设置 cacheNames 后，禁止动态创建 Cache
        CaffeineCacheManager m = new CaffeineCacheManager(CAFFEINE_5_MINUTES);
        // 16777216 = 16*1024*1024
        String caffeineSpec = "maximumSize=16777216,expireAfterWrite=5m";
        m.setCaffeineSpec(CaffeineSpec.parse(caffeineSpec));
        // spring-cache 使用 @Cacheable / @CachePut 等注解实现数据回源。目前暂未找到可以和 Caffeine 的 CacheLoader 融合的使用方式
        // m.setCacheLoader();
        return m;
    }

}
