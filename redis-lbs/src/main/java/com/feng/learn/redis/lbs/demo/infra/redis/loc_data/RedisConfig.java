package com.feng.learn.redis.lbs.demo.infra.redis.loc_data;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author zhanfeng.zhang
 * @date 2020/06/14
 */
@Configuration
public class RedisConfig {

  // keys
  public static final String LOCATION_DATA_PREFIX = "loc:d:"; // location:data
  public static final String LOCATION_GEO_QUEUE_PREFIX = "loc:geo:"; // location:geo
  private static final String RESOURCE_ID = "redis.local";
  public static final String CONNECTION_FACTORY = RESOURCE_ID + "RedisConnectionFactory";
  public static final String REDIS_TEMPLATE = RESOURCE_ID + "RedisTemplate";
  private static final String REDIS_SERVER_HOST = "127.0.0.1";
  private static final int REDIS_SERVER_PORT = 6379;

  @Bean(CONNECTION_FACTORY)
  public RedisConnectionFactory redisConnectionFactory() {
    return new JedisConnectionFactory();
  }


  @Bean(REDIS_TEMPLATE)
  public RedisTemplate redisTemplate(
      @Qualifier(CONNECTION_FACTORY) RedisConnectionFactory factory) {
    RedisTemplate redisTemplate = new RedisTemplate();
    redisTemplate.setConnectionFactory(factory);
    RedisSerializer<String> stringSerializer = new StringRedisSerializer();
    redisTemplate.setKeySerializer(stringSerializer);
    redisTemplate.setValueSerializer(stringSerializer);
    redisTemplate.setHashKeySerializer(stringSerializer);
    redisTemplate.setHashValueSerializer(stringSerializer);
    return redisTemplate;
  }

}
