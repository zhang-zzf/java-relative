package com.github.zzf.dd.repo.redis;

import static com.github.zzf.dd.repo.redis.config.SpringRedisCacheConfig.APP_PREFIX_TTL_30_MINUTES;
import static com.github.zzf.dd.repo.redis.config.SpringRedisCacheConfig.CACHE_MANAGER;
import static com.github.zzf.dd.repo.redis.config.SpringRedisCacheConfig.CACHE_REDIS_TTL_10_MINUTES;
import static com.github.zzf.dd.repo.redis.config.SpringRedisCacheConfig.CACHE_REDIS_TTL_30_MINUTES;
import static com.github.zzf.dd.repo.redis.config.SpringRedisCacheConfig.CACHE_REDIS_TTL_5_MINUTES;
import static com.github.zzf.dd.repo.redis.config.SpringRedisCacheConfig.CACHE_REDIS_TTL_8_MINUTES;
import static com.github.zzf.dd.repo.redis.config.SpringRedisCacheConfig.TTL_30_MINUTES;
import static com.github.zzf.dd.utils.MetricsUtil.logEvent;
import static com.github.zzf.dd.utils.MetricsUtil.timer;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Stream.ofNullable;

import com.github.zzf.dd.redis_multi_get.SomeMultiGetService;
import com.github.zzf.dd.redis_multi_get.SomeMultiGetServiceImpl;
import com.github.zzf.dd.user.model.User;
import com.github.zzf.dd.user.model.User.Address;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.validation.constraints.NotEmpty;
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
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-21
 */
@SuppressWarnings({"NullableProblems", "unchecked"})
@Slf4j
@Service
@Primary
@RequiredArgsConstructor
@CacheConfig(cacheManager = CACHE_MANAGER, cacheNames = {CACHE_REDIS_TTL_30_MINUTES})
public class SomeMultiGetServiceCacheImpl implements SomeMultiGetService {

    final @Qualifier(SomeMultiGetServiceImpl.BEAN) SomeMultiGetService delegator;
    final @Qualifier(REDIS_TEMPLATE) RedisTemplate<String, User> redisTemplate;

    @Override
    public List<User> batchGetBy(String area, List<@NotEmpty String> ids) {
        timer("batchGetBy", ids.size(), "stage", "fetch"); // 打点
        // lettuce pipeline 底层使用异步
        // pipeline 一次可以支持 10K 个批量查询，一般远远超过业务请求数量
        List<User> cachedData = fetchFromCache(area, ids).collect(toUnmodifiableList());
        if (cachedData.size() == ids.size()) { // all hit cache
            return cachedData;
        }
        List<String> cached = cachedData.stream().map(User::getUserNo).collect(toList());
        List<String> missed = ids.stream().filter(d -> !cached.contains(d)).collect(toList());
        timer("batchGetBy", missed.size(), "stage", "missed"); // 打点
        List<User> dbData = delegator.batchGetBy(area, missed);
        // pipeline 批量更新
        updateCache(dbData);
        // 缓存穿透 cache penetration
        logCachePenetration(missed, dbData);
        return Stream.concat(cachedData.stream(), dbData.stream()).collect(toList());
    }

    private Stream<User> fetchFromCache(String area, Collection<String> ids) {
        log.info("fetchFromCache -> ids: {}", ids);
        // lettuce pipeline 底层使用异步 实测 lettuce key 可以跨 node
        List<Object> cacheData = redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                ids.forEach(id -> operations.opsForValue().get(toRedisKey(area, id)));
                return null;
            }
        });
        return ofNullable(cacheData)
            // .flatMap(list -> list.stream())
            .flatMap(Collection::stream)
            .map(d -> (User) d)
            .filter(Objects::nonNull);
    }

    private void updateCache(Collection<User> dbData) {
        if (dbData.isEmpty()) {
            return;
        }
        redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                dbData.forEach(d -> {
                    String area = Optional.ofNullable(d.getAddress()).map(Address::getProvince).orElse("");
                    operations.opsForValue().set(toRedisKey(area, d.getUserNo()), d, TTL_30_MINUTES);
                });
                return null;
            }
        });
    }

    private void logCachePenetration(Collection<String> ids, Collection<User> dbData) {
        Set<String> dbIds = dbData.stream().map(User::getUserNo).collect(toSet());
        Set<String> dbMissed = ids.stream().filter(id -> !dbIds.contains(id)).collect(toSet());
        if (!dbMissed.isEmpty()) {
            logEvent("Cache", "Penetration", dbMissed, "method", "batchGetBy");
        }
    }

    @Override
    @Cacheable(key = "'d:a:'+ #area +':u:' + #userNo", cacheNames = {
        CACHE_REDIS_TTL_30_MINUTES,
        CACHE_REDIS_TTL_10_MINUTES,
        CACHE_REDIS_TTL_8_MINUTES,
        CACHE_REDIS_TTL_5_MINUTES,
    })
    public User getBy(String area, String userNo) {
        return delegator.getBy(area, userNo);
    }

    private String toRedisKey(String area, String id) {
        return APP_PREFIX_TTL_30_MINUTES + "d:a:" + area + ":u:" + id;
    }

    @CachePut(key = "'d:a:' + #area +':u:' + #d.userNo")
    public User cachePut(String area, User d) {
        log.info("cachePut -> {}, {}", area, d.getUserNo());
        return d;
    }

    public void batchEvictCacheByArea(String area) {
        final String pattern = APP_PREFIX_TTL_30_MINUTES + "d:a:" + area + ":u:*";
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).build();
            try (Cursor<byte[]> cursor = connection.scan(scanOptions)) {
                while (cursor.hasNext()) { // 删除所有满足条件的 key
                    byte[] key = cursor.next(); // 返回的是单个 key
                    log.info("scan -> pattern: {}, key: {}", pattern,
                        redisTemplate.getKeySerializer().deserialize(key));
                    connection.del(key);
                }
            } catch (IOException e) {// ignore
            }
            return null;
        });
    }

    private SomeRepoRedisPipelineCacheImplOptimize1 self;

    @Autowired
    @Lazy
    public void setSelf(SomeRepoRedisPipelineCacheImplOptimize1 self) {
        this.self = self;
    }

    private static final String REDIS_TEMPLATE = "RedisTemplate_SomeMultiGetServiceCacheImpl";

    @SuppressWarnings("rawtypes")
    @Configuration
    public static class RedisTemplateAutowire {
        @Bean(REDIS_TEMPLATE)
        public RedisTemplate<String, User> userRedisTemplate(RedisTemplate redisTemplate) {
            return redisTemplate;
        }
    }

}


