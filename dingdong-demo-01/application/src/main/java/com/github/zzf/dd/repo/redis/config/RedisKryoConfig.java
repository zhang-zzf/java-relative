package com.github.zzf.dd.repo.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date 2024-12-23
 */
@Configuration
public class RedisKryoConfig {

    public static final RedisSerializer<String> STRING_REDIS_SERIALIZER = RedisSerializer.string();

    /**
     * 直接放弃
     */
    /**
     * <pre>
     *     Could not deserialize object from Kryo;
     *     nested exception is com.esotericsoftware.kryo.KryoException: Class cannot be created (missing no-arg constructor):
     *     com.github.zzf.dd.common.TypeBean$TypeBeanSubClass$1
     *     Note: This is an anonymous class, which is not serializable by default in Kryo. Possible solutions:
     *     1. Remove uses of anonymous classes, including double brace initialization, from the containing class. This is the safest solution, as anonymous classes don't have predictable names for serialization.
     *     2. Register a FieldSerializer for the containing class and call FieldSerializer.setIgnoreSyntheticFields(false) on it. This is not safe but may be sufficient temporarily.
     *     Serialization trace:\nstringList (com.github.zzf.dd.common.TypeBean$TypeBeanSubClass)\ntypeBeanSubClass (com.github.zzf.dd.common.TypeBean)\ntypeBean (com.github.zzf.dd.user.model.User)
     * </pre>
     * Could not deserialize object from Kryo; nested exception is com.esotericsoftware.kryo.KryoException: Class cannot be created (missing no-arg constructor): com.github.zzf.dd.common.TypeBean$TypeBeanSubClass$1\nNote: This is an anonymous class, which is not serializable by default in Kryo. Possible solutions:\n1. Remove uses of anonymous classes, including double brace initialization, from the containing\nclass. This is the safest solution, as anonymous classes don't have predictable names for serialization.\n2. Register a FieldSerializer for the containing class and call FieldSerializer\nsetIgnoreSyntheticFields(false) on it. This is not safe but may be sufficient temporarily.\nSerialization trace:\nstringList (com.github.zzf.dd.common.TypeBean$TypeBeanSubClass)\ntypeBeanSubClass (com.github.zzf.dd.common.TypeBean)\ntypeBean (com.github.zzf.dd.user.model.User)
     */
    public static final RedisSerializer<Object> VALUE_SERIALIZER = new KryoRedisSerializer();
    public static final String REDIS_TEMPLATE = "redisTemplate_kryo";

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
