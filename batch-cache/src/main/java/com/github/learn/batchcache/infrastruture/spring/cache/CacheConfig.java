package com.github.learn.batchcache.infrastruture.spring.cache;

import static com.github.learn.batchcache.infrastruture.redis.RedisConfig.REDIS_FACTORY;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.google.common.collect.Sets;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * @author zhanfeng.zhang
 * @date 2020/7/24
 */
@Configuration
@EnableCaching
public class CacheConfig {

  public static final String REDIS_CACHE_MANAGER = "redisCacheManager";

  public static final String REDIS_CACHE_NAME_1 = "redis_1";
  public static final String REDIS_CACHE_NAME_2 = "redis_2";
  private static final RedisCacheConfiguration REDIS_CACHE_NAME_1_CONF = RedisCacheConfiguration
      .defaultCacheConfig()
      .serializeValuesWith(
          RedisSerializationContext.SerializationPair.fromSerializer(
              new GenericFastJsonRedisSerializer()))
      .entryTtl(Duration.ofMinutes(4));
  private static final RedisCacheConfiguration REDIS_CACHE_NAME_2_CONF = RedisCacheConfiguration
      .defaultCacheConfig()
      .serializeValuesWith(
          RedisSerializationContext.SerializationPair.fromSerializer(
              new GenericFastJsonRedisSerializer()))
      .entryTtl(Duration.ofMinutes(2));

  @Bean(REDIS_CACHE_MANAGER)
  public RedisCacheManager redisCacheManager(
      @Qualifier(REDIS_FACTORY) RedisConnectionFactory factory) {
    RedisCacheConfiguration defaultConf = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(16))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericFastJsonRedisSerializer()));
    return RedisCacheManager.builder(factory)
        .disableCreateOnMissingCache()
        .initialCacheNames(Sets.newHashSet()).cacheDefaults(defaultConf)
        .withCacheConfiguration(REDIS_CACHE_NAME_1, REDIS_CACHE_NAME_1_CONF)
        .withCacheConfiguration(REDIS_CACHE_NAME_2, REDIS_CACHE_NAME_2_CONF)
        .build();
  }

}
