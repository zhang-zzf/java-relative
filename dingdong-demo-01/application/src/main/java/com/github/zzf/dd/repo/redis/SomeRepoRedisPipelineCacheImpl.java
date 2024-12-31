package com.github.zzf.dd.repo.redis;

import com.github.zzf.dd.redis_multi_get.repo.SomeRepo;
import com.github.zzf.dd.user.model.User;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

import static com.github.zzf.dd.common.spring.async.ThreadPoolForRedisCache.ASYNC_THREAD;
import static com.github.zzf.dd.repo.redis.config.SpringRedisCacheConfig.APP_PREFIX;
import static com.github.zzf.dd.repo.redis.config.SpringRedisCacheConfig.CACHE_MANAGER;
import static com.github.zzf.dd.repo.redis.config.SpringRedisCacheConfig.CACHE_REDIS_TTL_5_MINUTES;
import static java.util.concurrent.CompletableFuture.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Stream.empty;
import static java.util.stream.Stream.ofNullable;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-10
 */
@Slf4j
// @Primary
@Repository
@RequiredArgsConstructor
@CacheConfig(cacheManager = CACHE_MANAGER, cacheNames = {CACHE_REDIS_TTL_5_MINUTES})
public class SomeRepoRedisPipelineCacheImpl implements SomeRepo {

    final @Qualifier("someRepoImpl") SomeRepo delegator;
    final @Qualifier(ASYNC_THREAD) Executor executor;
    final @Qualifier(REDIS_TEMPLATE) RedisTemplate<String, User> userRedisTemplate;

    @Override
    public List<User> getBy(String area, List<String> userNoList) {
        // List<User> cachedData = fetchFromCacheWithKeyNumLimit(area, userNoList);
        // lettuce pipeline 底层使用异步
        // pipeline 一次可以支持 10K 个批量查询，一般远远超过业务请求数量
        List<User> cachedData = fetchFromCache(area, userNoList).collect(toUnmodifiableList());
        if (cachedData.size() == userNoList.size()) { // all hit cache
            return cachedData;
        }
        List<String> cached = cachedData.stream().map(User::getUserNo).collect(toList());
        List<String> missed = userNoList.stream().filter(d -> !cached.contains(d)).collect(toList());
        List<User> dbData = delegator.getBy(area, missed);
        dbData.forEach(d -> runAsync(() -> self.cachePut(area, d), executor));
        return Stream.concat(cachedData.stream(), dbData.stream()).collect(toList());
    }

    private List<User> fetchFromCacheWithKeyNumLimit(String area, List<String> userNoList) {
        List<User> cachedData = Lists.partition(userNoList, 2).stream()
            // async get from cache
            .map(list -> supplyAsync(() -> fetchFromCache(area, list), executor))
            // combine all the future task
            .reduce(completedFuture(empty()), (a, b) -> a.thenCombine(b, Stream::concat))
            // wait for task done
            .join()
            .collect(toList());
        return cachedData;
    }

    @Override
    @Cacheable(key = "'u:' + #userNo")
    public User getBy(String area, String userNo) {
        return delegator.getBy(area, userNo);
    }

    @CachePut(key = "'u:' + #d.userNo")
    public User cachePut(String area, User d) {
        return d;
    }

    private SomeRepoRedisPipelineCacheImpl self;

    @Autowired
    @Lazy
    public void setSelf(SomeRepoRedisPipelineCacheImpl self) {
        this.self = self;
    }

    private Stream<User> fetchFromCache(String area, List<String> userNoList) {
        log.info("fetchFromCache -> userNoList: {}", userNoList);
        List<byte[]> redisKeyList = userNoList.stream()
            .map(id -> APP_PREFIX + "u:" + id)
            .map(key -> userRedisTemplate.getStringSerializer().serialize(key))
            .collect(toList());
        // 实测 lettuce key 可以跨 node
        List<Object> userList = userRedisTemplate.executePipelined((RedisCallback<?>) connection -> {
            redisKeyList.forEach(connection::get);
            return null;
        });
        return ofNullable(userList)
            .flatMap(Collection::stream)
            .map(d -> (User) d)
            .filter(Objects::nonNull);
    }

    private static final String REDIS_TEMPLATE = "RedisTemplate_SomeRepoRedisPipelineCacheImpl";

    @Configuration
    public static class RedisTemplateAutowire {
        @Bean(REDIS_TEMPLATE)
        public RedisTemplate<String, User> redisTemplate(RedisTemplate redisTemplate) {
            return redisTemplate;
        }
    }

}
