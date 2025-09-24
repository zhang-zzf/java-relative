package com.github.zzf.dd.common;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

@SuppressWarnings({"UnusedReturnValue", "NullableProblems", "unchecked", "unused"})
@Validated
public interface BatchCacheableRedis<ID, E> extends BatchCacheable<Set<ID>, ID, E> {

    RedisTemplate<String, E> redisTemplate();

    @Override
    default Stream<E> batchFetchCache(List<String> keyList) {
        List<E> redisValueList = redisTemplate().opsForValue().multiGet(keyList);
        return ofNullable(redisValueList)
                .stream()
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                ;
    }

    @Override
    default List<Object> batchUpdateCache(Map<String, E> keyToData, Duration ttl) {
        return redisTemplate().executePipelined(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                keyToData.forEach((redisKey, data) -> {
                    operations.opsForValue().set(redisKey, data, randomDuration(ttl));
                });
                return null;
            }
        });
    }

    @Override
    default void batchEvictCache(List<String> keyList) {
        redisTemplate().delete(keyList);
    }

}
