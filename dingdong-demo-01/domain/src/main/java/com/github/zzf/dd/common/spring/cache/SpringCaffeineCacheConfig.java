package com.github.zzf.dd.common.spring.cache;

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

    public static final String MANAGER = "CacheManager/repo/caffeine";

    /**
     * 保留 1 day
     */
    @Bean(MANAGER)
    @Primary // 存在多个 CacheManager 时必须制定一个默认的 CacheManager
    public CacheManager jvmCacheManager() {
        CaffeineCacheManager m = new CaffeineCacheManager();
        String caffeineSpec = "maximumSize=-1,expireAfterAccess=24h,expireAfterWrite=24h";
        m.setCaffeineSpec(CaffeineSpec.parse(caffeineSpec));
        return m;
    }

}
