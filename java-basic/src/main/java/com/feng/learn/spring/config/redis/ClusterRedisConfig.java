package com.feng.learn.spring.config.redis;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/05
 */
@Configuration
public class ClusterRedisConfig {

    public static final String REDIS_FACTORY = "cluster";
    public static final String STRING_REDIS_TEMPLATE = REDIS_FACTORY + "StringRedisTemplate";
    public static final String REDIS_TEMPLATE = REDIS_FACTORY + "RedisTemplate";


    @Bean(REDIS_FACTORY)
    public RedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory(new RedisClusterConfiguration()
            .clusterNode("127.0.0.1", 7700));
    }

    @Bean(STRING_REDIS_TEMPLATE)
    public StringRedisTemplate stringRedisTemplate(
        @Qualifier(REDIS_FACTORY) RedisConnectionFactory factory) {
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
