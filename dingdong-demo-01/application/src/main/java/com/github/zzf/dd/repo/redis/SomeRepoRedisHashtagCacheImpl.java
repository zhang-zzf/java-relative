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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

import static com.github.zzf.dd.common.spring.async.ThreadPoolForRedisCache.ASYNC_THREAD;
import static com.github.zzf.dd.common.spring.cache.SpringCacheConfig.CACHE_MANAGER_FOR_REDIS;
import static com.github.zzf.dd.common.spring.cache.SpringCacheConfig.CACHE_REDIS_TTL_5_MINUTES;
import static com.github.zzf.dd.repo.redis.config.SpringRedisCacheConfig.APP_PREFIX;
import static java.util.concurrent.CompletableFuture.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.empty;
import static java.util.stream.Stream.ofNullable;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-10
 */
@Repository
@CacheConfig(cacheManager = CACHE_MANAGER_FOR_REDIS, cacheNames = {CACHE_REDIS_TTL_5_MINUTES})
@Slf4j
@RequiredArgsConstructor
public class SomeRepoRedisHashtagCacheImpl implements SomeRepo {

    final @Qualifier("someRepoImpl") SomeRepo delegator;
    final @Qualifier(ASYNC_THREAD) Executor executor;
    final @Qualifier(USER_REDIS_TEMPLATE) RedisTemplate<String, User> userRedisTemplate;

    /**
     * <pre>
     *     1. 批量获取接口在业务上可以通过分片规则（通过 hashtag ）路由到同一个 redis node 上
     *     1. spring cache key 使用同样的规则生成  key = "'a:{' + #area + '}:u:' + #userNo"
     * </pre>
     */
    @Override
    public List<User> getBy(String area, List<String> userNoList) {
        List<User> cachedData = Lists.partition(userNoList, 2).stream()
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
        List<User> dbData = delegator.getBy(area, missed);
        dbData.forEach(d -> runAsync(() -> self.cachePut(area, d), executor));
        return Stream.concat(cachedData.stream(), dbData.stream()).collect(toList());
    }

    @Override
    @Cacheable(key = "'a:{' + #area + '}:u:' + #userNo")
    public User getBy(String area, String userNo) {
        return delegator.getBy(area, userNo);
    }

    @CachePut(key = "'a:{' + #area + '}:u:' + #d.userNo")
    public User cachePut(String area, User d) {
        return d;
    }

    private SomeRepoRedisHashtagCacheImpl self;

    @Autowired
    @Lazy
    public void setSelf(SomeRepoRedisHashtagCacheImpl self) {
        this.self = self;
    }

    private Stream<User> fetchFromCache(String area, List<String> userNoList) {
        log.info("fetchFromCache -> userNoList: {}", userNoList);
        List<String> redisKeyList = userNoList.stream()
            .map(id -> APP_PREFIX + "a:{" + area + "}:u:" + id)
            .collect(toList());
        List<User> userList = userRedisTemplate.opsForValue().multiGet(redisKeyList);
        return ofNullable(userList)
            .flatMap(Collection::stream)
            .filter(Objects::nonNull);
    }

    private static final String USER_REDIS_TEMPLATE
        = "SomeRepoRedisHashtagCacheImplUserRedisTemplate";

    @Configuration
    public static class RedisTemplateAutowire {
        @Bean(USER_REDIS_TEMPLATE)
        public RedisTemplate<String, User> userRedisTemplate(RedisTemplate redisTemplate) {
            return redisTemplate;
        }
    }

}
