package com.github.zzf.dd.repo;

import static com.github.zzf.dd.common.spring.async.ThreadPoolForRedisCache.ASYNC_THREAD;
import static com.github.zzf.dd.common.spring.cache.SpringCacheConfig.CACHE_MANAGER_FOR_REDIS;
import static com.github.zzf.dd.common.spring.cache.SpringCacheConfig.CACHE_REDIS_TTL_5_MINUTES;
import static com.github.zzf.dd.repo.redis.spring.cache.SpringRedisCacheConfig.APP_PREFIX;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.empty;
import static java.util.stream.Stream.ofNullable;

import com.github.zzf.dd.user.model.User;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-10
 */
@Repository
@CacheConfig(cacheManager = CACHE_MANAGER_FOR_REDIS, cacheNames = {CACHE_REDIS_TTL_5_MINUTES})
@Slf4j
@Primary
public class SomeRepoRedisPipelineCacheImpl extends SomeRepoImpl {

    final Executor executor;
    final RedisTemplate<String, User> userRedisTemplate;

    public SomeRepoRedisPipelineCacheImpl(@Qualifier(ASYNC_THREAD) Executor executor,
        @Qualifier(USER_REDIS_TEMPLATE) RedisTemplate<String, User> userRedisTemplate) {
        super(executor);
        this.executor = executor;
        this.userRedisTemplate = userRedisTemplate;
    }

    /**
     * <pre>
     * </pre>
     */
    @Override
    public List<User> getBy(String area, List<String> userNoList) {
        List<User> cachedData = Lists.partition(userNoList, BATCH_SIZE).stream()
            // async get from cache
            .map(list -> supplyAsync(() -> fetchFromCache(area, list), executor))
            // combine all the future task
            .reduce(completedFuture(empty()), (a, b) -> a.thenCombine(b, Stream::concat))
            // wait for task done
            .join()
            .collect(toList());
        if (cachedData.size() == userNoList.size()) { // all hit cache
            return cachedData;
        }
        List<String> cached = cachedData.stream().map(User::getUserNo).collect(toList());
        List<String> missed = userNoList.stream().filter(d -> !cached.contains(d)).collect(toList());
        List<User> dbData = super.getBy(area, missed);
        dbData.forEach(d -> runAsync(() -> self.cachePut(area, d), executor));
        return Stream.concat(cachedData.stream(), dbData.stream()).collect(toList());
    }

    @Override
    @Cacheable(key = "'u:' + #userNo")
    public User getBy(String area, String userNo) {
        return super.getBy(area, userNo);
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
        List<Object> userList = userRedisTemplate.executePipelined((RedisCallback<List<User>>) connection -> {
            redisKeyList.forEach(connection::get);
            return null;
        });
        return ofNullable(userList)
            .flatMap(Collection::stream)
            .map(d -> (User) d)
            .filter(Objects::nonNull);
    }

    private static final String USER_REDIS_TEMPLATE
        = "SomeRepoRedisPipelineCacheImplUserRedisTemplate";

    @Configuration
    public static class RedisTemplateAutowire {
        @Bean(USER_REDIS_TEMPLATE)
        public RedisTemplate<String, User> userRedisTemplate(RedisTemplate redisTemplate) {
            return redisTemplate;
        }
    }

}
