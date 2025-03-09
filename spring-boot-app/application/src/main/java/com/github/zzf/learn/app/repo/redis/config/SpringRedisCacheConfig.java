package com.github.zzf.learn.app.repo.redis.config;

import static com.github.zzf.learn.app.repo.redis.config.RedisConfig.STRING_REDIS_SERIALIZER;
import static com.github.zzf.learn.app.repo.redis.config.RedisConfig.VALUE_SERIALIZER;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

import java.time.Duration;
import java.util.HashMap;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@EnableCaching
public class SpringRedisCacheConfig {

    // cache manage for redis
    public static final String CACHE_MANAGER = "CacheManager/repo/redis";
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

    public static final String APP_PREFIX = "app:";
    public static final String APP_PREFIX_TTL_5_MINUTES = APP_PREFIX + "json:";
    public static final String APP_PREFIX_TTL_8_MINUTES = APP_PREFIX + "gzip:";
    public static final String APP_PREFIX_TTL_10_MINUTES = APP_PREFIX + "lz4:";
    public static final String APP_PREFIX_TTL_30_MINUTES = APP_PREFIX + "msgpack:";

    @Bean(CACHE_MANAGER)
    // @Primary // 存在多个 CacheManager 时必须制定一个默认的 CacheManager
    // public RedisCacheManager cacheManager(RedisConnectionFactory cf) {
    public RedisCacheManager cacheManager(RedisConnectionFactory cf) {
        // return RedisCacheManager.create(cf);
        return RedisCacheManager.builder(cf)
            .cacheDefaults(defaultConfiguration())
            .disableCreateOnMissingCache() // 禁止自动创建
            // .initialCacheNames(Set.of(CACHE_REDIS_TTL_5_MINUTES))
            .withInitialCacheConfigurations(new HashMap<>() {{
                // cacheName <-> Configuration
                put(CACHE_REDIS_TTL_5_MINUTES, defaultConfiguration()
                    .prefixCacheNameWith(APP_PREFIX_TTL_5_MINUTES));
                put(CACHE_REDIS_TTL_8_MINUTES, defaultConfiguration()
                    .prefixCacheNameWith(APP_PREFIX_TTL_8_MINUTES)
                    .entryTtl(TTL_8_MINUTES)
                    .serializeKeysWith(fromSerializer(RedisGzipCompressConfig.STRING_REDIS_SERIALIZER))
                    .serializeValuesWith(fromSerializer(RedisGzipCompressConfig.VALUE_SERIALIZER))
                );
                put(CACHE_REDIS_TTL_10_MINUTES, defaultConfiguration()
                    .prefixCacheNameWith(APP_PREFIX_TTL_10_MINUTES)
                    .entryTtl(TTL_10_MINUTES)
                    .serializeKeysWith(fromSerializer(RedisLz4CompressConfig.STRING_REDIS_SERIALIZER))
                    .serializeValuesWith(fromSerializer(RedisLz4CompressConfig.VALUE_SERIALIZER))
                );
                put(CACHE_REDIS_TTL_30_MINUTES, defaultConfiguration()
                    .prefixCacheNameWith(APP_PREFIX_TTL_30_MINUTES)
                    .entryTtl(TTL_30_MINUTES)
                    .serializeKeysWith(fromSerializer(RedisMsgPackConfig.STRING_REDIS_SERIALIZER))
                    .serializeValuesWith(fromSerializer(RedisMsgPackConfig.VALUE_SERIALIZER)));
            }})
            .build();
    }

    private static RedisCacheConfiguration defaultConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .prefixCacheNameWith(APP_PREFIX)
            .entryTtl(Duration.ofMinutes(5))
            // 这里配置 Serializer 和 RedisTemplate 保持一致
            .serializeKeysWith(fromSerializer(STRING_REDIS_SERIALIZER))
            .serializeValuesWith(fromSerializer(VALUE_SERIALIZER))
            ;
    }

}
