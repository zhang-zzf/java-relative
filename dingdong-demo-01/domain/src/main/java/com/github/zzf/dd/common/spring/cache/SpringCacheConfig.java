package com.github.zzf.dd.common.spring.cache;

import java.time.Duration;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-10
 */
public class SpringCacheConfig {
    // 只定义需要的 Cache，不定义具体的实现

    // cache manage for redis
    public static final String CACHE_MANAGER_FOR_REDIS = "CacheManager/repo/redis";
    // cache for 5 minutes
    public static final String CACHE_REDIS_TTL_5_MINUTES = "REDIS_TTL_5_MINUTES";
    public static final Duration TTL_5_MINUTES = Duration.ofMinutes(5);
    public static final String CACHE_REDIS_TTL_8_MINUTES = "REDIS_TTL_8_MINUTES";
    public static final Duration TTL_8_MINUTES = Duration.ofMinutes(8);
    public static final String CACHE_REDIS_TTL_10_MINUTES = "REDIS_TTL_10_MINUTES";
    public static final Duration TTL_10_MINUTES = Duration.ofMinutes(10);
    // public static final String CACHE_REDIS_TTL_15_MINUTES = "REDIS_TTL_15_MINUTES";
    public static final Duration TTL_15_MINUTES = Duration.ofMinutes(15);
    public static final String CACHE_REDIS_TTL_30_MINUTES = "REDIS_TTL_30_MINUTES";
    public static final Duration TTL_30_MINUTES = Duration.ofMinutes(30);

}
