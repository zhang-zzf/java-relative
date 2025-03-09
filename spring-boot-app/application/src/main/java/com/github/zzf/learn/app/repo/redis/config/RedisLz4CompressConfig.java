package com.github.zzf.learn.app.repo.redis.config;

import static com.github.zzf.learn.app.repo.redis.config.RedisConfig.objectMapper;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.LZ4_FRAMED;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-10
 */
@Configuration
public class RedisLz4CompressConfig {

    public static final RedisSerializer<String> STRING_REDIS_SERIALIZER = RedisSerializer.string();
    public static final RedisSerializer<Object> VALUE_SERIALIZER
        = new RedisSerializerCompressWrapper(new GenericJackson2JsonRedisSerializer(objectMapper()), LZ4_FRAMED);
    public static final String REDIS_TEMPLATE = "redisTemplate_lz4";

    @Bean(REDIS_TEMPLATE)
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

}
