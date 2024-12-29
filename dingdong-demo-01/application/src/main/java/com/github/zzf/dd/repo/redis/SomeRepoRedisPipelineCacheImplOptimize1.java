package com.github.zzf.dd.repo.redis;

import static com.github.zzf.dd.common.spring.async.ThreadPoolForRedisCache.ASYNC_THREAD;
import static com.github.zzf.dd.repo.redis.config.SpringRedisCacheConfig.APP_PREFIX;
import static com.github.zzf.dd.repo.redis.config.SpringRedisCacheConfig.CACHE_MANAGER;
import static com.github.zzf.dd.repo.redis.config.SpringRedisCacheConfig.CACHE_REDIS_TTL_30_MINUTES;
import static com.github.zzf.dd.repo.redis.config.SpringRedisCacheConfig.CACHE_REDIS_TTL_5_MINUTES;
import static com.github.zzf.dd.repo.redis.config.SpringRedisCacheConfig.TTL_30_MINUTES;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Stream.empty;
import static java.util.stream.Stream.ofNullable;

import com.github.zzf.dd.redis_multi_get.repo.SomeRepo;
import com.github.zzf.dd.user.model.User;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
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
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Repository;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-10
 */
@Slf4j
// @Primary
@Repository
@RequiredArgsConstructor
@CacheConfig(cacheManager = CACHE_MANAGER, cacheNames = {CACHE_REDIS_TTL_5_MINUTES})
public class SomeRepoRedisPipelineCacheImplOptimize1 implements SomeRepo {

    final @Qualifier("someRepoImpl") SomeRepo delegator;
    final @Qualifier(ASYNC_THREAD) Executor executor;
    final @Qualifier(REDIS_TEMPLATE) RedisTemplate<String, User> redisTemplate;

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
        // dbData.forEach(d -> runAsync(() -> self.cachePut(area, d), executor));
        //
        // 优化点：pipeline 批量更新
        executor.execute(() -> updateCache(dbData));
        return Stream.concat(cachedData.stream(), dbData.stream()).collect(toList());
    }

    private void updateCache(List<User> dbData) {
        // todo record
        redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                dbData.forEach(d -> operations.opsForValue().set(toRedisKey(d.getUserNo()), d, TTL_30_MINUTES));
                return null;
            }
        });
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
    @Cacheable(key = "'u:' + #userNo", cacheNames = {CACHE_REDIS_TTL_5_MINUTES, CACHE_REDIS_TTL_30_MINUTES})
    public User getBy(String area, String userNo) {
        return delegator.getBy(area, userNo);
    }

    @CachePut(key = "'u:' + #d.userNo")
    public User cachePut(String area, User d) {
        return d;
    }

    private SomeRepoRedisPipelineCacheImplOptimize1 self;

    @Autowired
    @Lazy
    public void setSelf(SomeRepoRedisPipelineCacheImplOptimize1 self) {
        this.self = self;
    }

    private Stream<User> fetchFromCache(String area, List<String> userNoList) {
        log.info("fetchFromCache -> userNoList: {}", userNoList);
        // List<byte[]> redisKeyList = userNoList.stream()
        //     .map(id -> toRedisKey(id))
        //     .map(key -> redisTemplate.getStringSerializer().serialize(key))
        //     .collect(toList());
        // 实测 lettuce key 可以跨 node
        // List<Object> userList = userRedisTemplate.executePipelined((RedisCallback<List<User>>) connection -> {
        //     redisKeyList.forEach(connection::get);
        //     return null;
        // });
        //
        // 优化点： 使用 SessionCallback
        // lettuce pipeline 底层使用异步
        // 实测 lettuce key 可以跨 node
        List<Object> userList = redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                userNoList.forEach(id -> operations.opsForValue().get(toRedisKey(id)));
                return null;
            }
        });
        return ofNullable(userList)
            .flatMap(Collection::stream)
            .map(d -> (User) d)
            .filter(Objects::nonNull);
    }

    private String toRedisKey(String id) {
        return APP_PREFIX + "u:" + id;
    }

    private static final String REDIS_TEMPLATE = "RedisTemplate_SomeRepoRedisPipelineCacheImplOptimize1";

    @Configuration
    public static class RedisTemplateAutowire {
        @Bean(REDIS_TEMPLATE)
        public RedisTemplate<String, User> redisTemplate(RedisTemplate redisTemplate) {
            return redisTemplate;
        }
    }

}
