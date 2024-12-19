package com.github.zzf.dd.repo.redis;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.SimpleDateFormat;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-10
 */
@Configuration
public class RedisConfig {

    public static final RedisSerializer<String> STRING_REDIS_SERIALIZER = RedisSerializer.string();

    public static final GenericJackson2JsonRedisSerializer VALUE_SERIALIZER
        = new GenericJackson2JsonRedisSerializer(objectMapper());

    private static ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 日期序列化
        mapper.registerModule(new JavaTimeModule());
        // java.util.Date / java.time.* 生效
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 会把 LocalDateTime 序列化成 []
        // mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);// 序列化成 timestamp
        mapper.setSerializationInclusion(Include.NON_NULL);
        // json String 中会有多个 @class 字段
        // mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, DefaultTyping.NON_FINAL, As.PROPERTY);
        //
        // 配置模型上 @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS) 只在最外层添加 @class 字段
        // 配置的意思在于：只有 Object 类型的字段会添加 @class 字段。
        // 主要用于反序列化时解析最外层 @class
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, DefaultTyping.JAVA_LANG_OBJECT, As.PROPERTY);
        return mapper;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(STRING_REDIS_SERIALIZER);
        // 用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        redisTemplate.setValueSerializer(VALUE_SERIALIZER);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(STRING_REDIS_SERIALIZER);
        // hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(VALUE_SERIALIZER);
        return redisTemplate;
    }

    @Configuration
    @Profile("jedisCluster")
    @EnableConfigurationProperties(RedisProperties.class)
    public static class JedisClusterConnectionFactory {

        public @Bean RedisConnectionFactory connectionFactory(RedisProperties redisProperties) {
            return new JedisConnectionFactory(new RedisClusterConfiguration(redisProperties.getCluster().getNodes()));
        }

    }

}
