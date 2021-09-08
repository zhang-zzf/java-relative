package com.feng.learn.spring.config.redis;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/05
 */
@Configuration
public class StandaloneRedisConfig {

    public static final String REDIS_FACTORY = "standalone";
    public static final String STRING_REDIS_TEMPLATE = REDIS_FACTORY + "StringRedisTemplate";
    public static final String REDIS_TEMPLATE = REDIS_FACTORY + "RedisTemplate";

    private static final String REDIS_SERVER_HOST = "127.0.0.1";
    private static final int REDIS_SERVER_PORT = 6379;

    @Bean(REDIS_FACTORY)
    public RedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory(
            new RedisStandaloneConfiguration(REDIS_SERVER_HOST, REDIS_SERVER_PORT));
    }

    @Bean(STRING_REDIS_TEMPLATE)
    public StringRedisTemplate stringRedisTemplate(@Qualifier(REDIS_FACTORY) RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }

    @Bean(REDIS_TEMPLATE)
    public RedisTemplate redisTemplate(@Qualifier(REDIS_FACTORY) RedisConnectionFactory factory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(new GenericFastJsonRedisSerializer());
        return redisTemplate;
    }

}
