package com.github.zzf.dd.repo.redis;


import static java.time.format.DateTimeFormatter.ofPattern;
import static org.assertj.core.api.Java6BDDAssertions.then;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.github.zzf.dd.user.model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-15
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("mini234b")
@Slf4j
public class RedisJacksonSerializeTest {

    static final String USER_REDIS_TEMPLATE = "USER_REDIS_TEMPLATE_RedisBasicTest";

    RedisTemplate<String, User> redisTemplate;

    /**
     * 测试 jackson 序列化
     * <pre>
     *      若序列化时不添加 @class 类信息，无法被反序列化
     *      class java.util.LinkedHashMap cannot be cast to class com.github.zzf.dd.user.model.User
     * </pre>
     */
    @Test
    public void givenJackson_whenSerializeAndDeserialize_then() {
        String number = "15618536513";
        User u = User.from("mobile", number);
        ValueOperations<String, User> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(u.getUserNo(), u);
        User redisData = redisTemplate.opsForValue().get(u.getUserNo());
        then(redisData).returns(number, User::getUsername);
    }

    @Autowired
    @Lazy
    public void setRedisTemplate(@Qualifier(USER_REDIS_TEMPLATE) RedisTemplate<String, User> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

}

@Configuration
class RedisTemplateAutowire {

    public static final RedisSerializer<String> STRING_REDIS_SERIALIZER = RedisSerializer.string();

    public static final GenericJackson2JsonRedisSerializer VALUE_SERIALIZER
        = new GenericJackson2JsonRedisSerializer(objectMapper());

    private static ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // 日期序列化
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.XXX";
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(ofPattern(pattern)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(ofPattern("HH:mm:ss")));
        // 日期反序列化
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(ofPattern(pattern)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(ofPattern("HH:mm:ss")));
        //
        mapper.registerModule(javaTimeModule);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);// 序列化成 timestamp
        mapper.setSerializationInclusion(Include.NON_NULL);
        // json string 中不添加类信息
        // mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, As.PROPERTY);
        return mapper;
    }

    @Bean(RedisJacksonSerializeTest.USER_REDIS_TEMPLATE)
    public RedisTemplate<String, User> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, User> redisTemplate = new RedisTemplate<>();
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


