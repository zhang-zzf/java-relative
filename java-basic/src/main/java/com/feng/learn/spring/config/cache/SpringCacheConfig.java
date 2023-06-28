package com.feng.learn.spring.config.cache;

import static com.feng.learn.spring.config.redis.StandaloneRedisConfig.REDIS_FACTORY;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.feng.learn.spring.config.cache.jvmredis.LocalRemoteComposeCacheManager;
import com.feng.learn.spring.config.redis.StandaloneRedisConfig;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Sets;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.cache.support.NoOpCache;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/14
 */
@Configuration
@Import(StandaloneRedisConfig.class)
@EnableCaching
public class SpringCacheConfig {

  public static final String REDIS_CACHE_MANAGER = "redisCacheManager";

  public static final String REDIS_CACHE_NAME_1 = "redis_1";
  public static final int REDIS_CACHE_NAME_1_TTL = 4;
  public static final String REDIS_CACHE_NAME_2 = "redis_2";
  public static final String LOCAL_REMOTE_CACHE_MANAGER = "localRemoteComposeCacheManager";
  public static final int LOCAL_REMOTE_CACHE_LOCAL_TTL = 2;
  public static final String CACHE_1 = "lr1";
  public static final String CACHE_2 = "lr2";
  private static final RedisCacheConfiguration REDIS_CACHE_NAME_1_CONF = RedisCacheConfiguration
      .defaultCacheConfig()
      .serializeValuesWith(SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()))
      .entryTtl(Duration.ofSeconds(REDIS_CACHE_NAME_1_TTL));
  private static final RedisCacheConfiguration REDIS_CACHE_NAME_2_CONF = RedisCacheConfiguration
      .defaultCacheConfig()
      .serializeValuesWith(SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()))
      .entryTtl(Duration.ofMinutes(2));

  @Bean(LOCAL_REMOTE_CACHE_MANAGER)
  public LocalRemoteComposeCacheManager localRemoteComposeCacheManager(
      @Qualifier(REDIS_FACTORY) RedisConnectionFactory factory) {
    // local cache
    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
    Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
        .expireAfterWrite(LOCAL_REMOTE_CACHE_LOCAL_TTL, TimeUnit.SECONDS)
        .maximumSize(1024);
    caffeineCacheManager.setCaffeine(caffeine);
    // remote cache
    RedisCacheConfiguration defaultConf = RedisCacheConfiguration.defaultCacheConfig()
        // 默认16分钟
        .entryTtl(Duration.ofMinutes(16))
        .serializeValuesWith(
            SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));
    RedisCacheManager redisCacheManager = RedisCacheManager.builder(factory)
        .cacheDefaults(defaultConf)
        // 特殊配置
        .withCacheConfiguration(CACHE_1, REDIS_CACHE_NAME_1_CONF)
        .withCacheConfiguration(CACHE_2, REDIS_CACHE_NAME_2_CONF)
        .build();
    // very important: do not forget to exec bean init logic.
    redisCacheManager.afterPropertiesSet();
    return new LocalRemoteComposeCacheManager(caffeineCacheManager, redisCacheManager);
  }

  @Bean
  public SimpleCacheManager simpleCacheManager() {
    // 需手动定义 Cache
    SimpleCacheManager manager = new SimpleCacheManager();

    NoOpCache noOpCache = new NoOpCache("noOpCache");
    ConcurrentMapCache concurrentMapCache = new ConcurrentMapCache("concurrentMapCache");

    manager.setCaches(Arrays.asList(noOpCache, concurrentMapCache));
    return manager;
  }


  @Bean
  @Primary
  public NoOpCacheManager noOpCacheManager() {
    return new NoOpCacheManager();
  }

  @Bean
  public CompositeCacheManager compositeCacheManager() {
    CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
    return compositeCacheManager;
  }

  @Bean(REDIS_CACHE_MANAGER)
  public RedisCacheManager redisCacheManager(
      @Qualifier(REDIS_FACTORY) RedisConnectionFactory factory) {
    RedisCacheConfiguration defaultConf = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(16))
        .serializeValuesWith(
            SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));
    return RedisCacheManager.builder(factory)
        .disableCreateOnMissingCache()
        .initialCacheNames(Sets.newHashSet()).cacheDefaults(defaultConf)
        .withCacheConfiguration(REDIS_CACHE_NAME_1, REDIS_CACHE_NAME_1_CONF)
        .withCacheConfiguration(REDIS_CACHE_NAME_2, REDIS_CACHE_NAME_2_CONF)
        .build();
  }

  @Bean
  public ConcurrentMapCacheManager concurrentMapCacheManager() {
    return new ConcurrentMapCacheManager();
  }

}
