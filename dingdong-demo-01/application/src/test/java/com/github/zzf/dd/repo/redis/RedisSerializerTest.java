package com.github.zzf.dd.repo.redis;


import static com.github.zzf.dd.repo.redis.SomeRepoRedisPipelineCacheImplOptimize1.REDIS_TEMPLATE;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.zzf.dd.user.model.User;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
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
public class RedisSerializerTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    @Qualifier(REDIS_TEMPLATE)
    RedisTemplate<String, User> userRedisTemplate;

    /**
     * <pre>
     *    测试 RedisCallback / SessionCallback 的区别
     *      1. RedisCallback 回调中的 RedisConnection 是基层 Connection，
     *      只能操作 byte[] 类型的数据，也就是说序列化操作需要业务编码实现。
     *      1. SessionCallback 回调中的 RedisOperations 实际上是 RedisTemplate 的接口。
     *      回调时 RedisOperations 实际上就是 RedisTemplate。可以使用 RedisTemplate 的所有能力，如序列化和反序列化
     * </pre>
     */
    @Test
    public void givenRedisTemplate_whenCallback_then() {
        User user = User.fromMobile("15618536513");
        // set key, val
        Boolean setResult = userRedisTemplate.execute(new RedisCallback<Boolean>() {
            @SuppressWarnings("unchecked")
            @Override
            @Nullable
            public Boolean doInRedis(@Nonnull RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> keySerializer = (RedisSerializer<String>) userRedisTemplate.getKeySerializer();
                RedisSerializer<User> valueSerializer = (RedisSerializer<User>) userRedisTemplate.getValueSerializer();
                byte[] key = keySerializer.serialize(user.getUserNo());
                byte[] val = valueSerializer.serialize(user);
                return connection.set(Objects.requireNonNull(key), Objects.requireNonNull(val));
            }
        });
        User cached = userRedisTemplate.execute(new RedisCallback<User>() {
            @SuppressWarnings("unchecked")
            @Override
            public User doInRedis(@Nonnull RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> keySerializer = (RedisSerializer<String>) userRedisTemplate.getKeySerializer();
                RedisSerializer<User> valueSerializer = (RedisSerializer<User>) userRedisTemplate.getValueSerializer();
                // 手动序列化
                byte[] key = keySerializer.serialize(user.getUserNo());
                byte[] result = connection.get(Objects.requireNonNull(key));
                // 手动反序列化
                return valueSerializer.deserialize(result);
            }
        });
        then(Objects.requireNonNull(cached).getUserNo()).isEqualTo(user.getUserNo());
        userRedisTemplate.delete(user.getUserNo());
        //
        // SessionCallback
        userRedisTemplate.execute(new SessionCallback<Void>() {
            @SuppressWarnings("unchecked")
            @Override
            public Void execute(@Nonnull RedisOperations operations) throws DataAccessException {
                operations.opsForValue().set(user.getUserNo(), user);
                return null;
            }
        });
        User cached2 = userRedisTemplate.execute(new SessionCallback<User>() {
            @Override
            public User execute(@Nonnull RedisOperations operations) throws DataAccessException {
                return (User) operations.opsForValue().get(user.getUserNo());
            }
        });
        then(Objects.requireNonNull(cached2).getUserNo()).isEqualTo(user.getUserNo());
        userRedisTemplate.delete(user.getUserNo());
    }

    /**
     * jackson 序列化的 json 串中若无 @class 字段，无法被 GenericJackson2JsonRedisSerializer 反序列化
     */
    @Test
    public void givenJackson_whenSerialize_then() {
        NoClassTypeInfo noClassTypeInfo = new NoClassTypeInfo().setStr("NoClassTypeInfo");
        ClassTypeInfo classTypeInfo = new ClassTypeInfo().setStr("ClassTypeInfo");
        redisTemplate.opsForValue().set("noClassTypeInfo", noClassTypeInfo);
        redisTemplate.opsForValue().set("classTypeInfo", classTypeInfo);
        // Could not read JSON: Could not resolve subtype of [simple type, class java.lang.Object]: missing type id property '@class' at [Source: (byte[])"{"str":"NoClassTypeInfo"}"; line: 1, column: 25]
        Throwable throwable = catchThrowable(() -> redisTemplate.opsForValue().get("noClassTypeInfo"));
        then(throwable).isNotNull();
        // 无法转换成 NoClassTypeInfo
        Object o2 = redisTemplate.opsForValue().get("classTypeInfo");
        then(o2).isNotNull().isInstanceOf(ClassTypeInfo.class);
    }

    @Data
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    public static class ClassTypeInfo {
        String str;
    }

    @Data
    public static class NoClassTypeInfo {
        String str;
    }

}