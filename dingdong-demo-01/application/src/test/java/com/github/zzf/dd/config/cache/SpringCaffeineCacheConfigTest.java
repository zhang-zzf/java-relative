package com.github.zzf.dd.config.cache;

import static org.assertj.core.api.Java6BDDAssertions.then;

import org.junit.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-25
 */
public class SpringCaffeineCacheConfigTest {


    @Test
    public void givenCaffeine_when_then() {
        SpringCaffeineCacheConfig cacheConfig = new SpringCaffeineCacheConfig();
        CacheManager cacheManager = cacheConfig.jvmCacheManager();
        Cache ttl5minutes = cacheManager.getCache("ttl5minutes");
        Cache ttl15minutes = cacheManager.getCache("ttl15minutes");
        then(cacheManager.getCacheNames().size()).isEqualTo(2);
    }

}