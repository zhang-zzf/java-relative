package com.github.zzf.dd.repo.redis.spring.cache;

import static com.github.zzf.dd.common.spring.cache.SpringCacheConfig.CACHE_MANAGER_FOR_REDIS;
import static com.github.zzf.dd.common.spring.cache.SpringCacheConfig.CACHE_REDIS_TTL_30_MINUTES;
import static com.github.zzf.dd.common.spring.cache.SpringCacheConfig.CACHE_REDIS_TTL_5_MINUTES;
import static com.github.zzf.dd.repo.redis.RedisConfig.STRING_REDIS_SERIALIZER;
import static com.github.zzf.dd.repo.redis.RedisConfig.VALUE_SERIALIZER;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

import com.github.zzf.dd.repo.redis.RedisMsgPackConfig;
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

    public static final String APP_PREFIX = "dd:";

    @Bean(CACHE_MANAGER_FOR_REDIS)
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
                put(CACHE_REDIS_TTL_5_MINUTES, defaultConfiguration().prefixKeysWith(APP_PREFIX + "json:"));
                put(CACHE_REDIS_TTL_30_MINUTES, defaultConfiguration()
                    .prefixKeysWith(APP_PREFIX + "msgpack:")
                    .entryTtl(Duration.ofMinutes(30))
                    .serializeKeysWith(fromSerializer(RedisMsgPackConfig.STRING_REDIS_SERIALIZER))
                    .serializeValuesWith(fromSerializer(RedisMsgPackConfig.VALUE_SERIALIZER)));
            }})
            .build();
    }

    private static RedisCacheConfiguration defaultConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .prefixKeysWith(APP_PREFIX)
            .entryTtl(Duration.ofMinutes(5))
            // 这里配置 Serializer 和 RedisTemplate 保持一致
            .serializeKeysWith(fromSerializer(STRING_REDIS_SERIALIZER))
            .serializeValuesWith(fromSerializer(VALUE_SERIALIZER))
            ;
    }

}
