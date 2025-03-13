package com.github.zzf.learn.app.repo;

import static com.github.zzf.learn.app.common.spring.async.ThreadPoolForStation.ASYNC_THREAD;
import static com.github.zzf.learn.app.common.spring.cache.SpringCaffeineCacheConfig.CAFFEINE_5_MINUTES;
import static com.github.zzf.learn.app.common.spring.cache.SpringCaffeineCacheConfig.CAFFEINE_STATION;
import static com.github.zzf.learn.app.repo.StationRepoMySQLImpl.BEAN_NAME;
import static com.github.zzf.learn.app.repo.redis.config.SpringRedisCacheConfig.APP_PREFIX;
import static com.github.zzf.learn.app.repo.redis.config.SpringRedisCacheConfig.CACHE_5_MINUTES;
import static com.github.zzf.learn.app.repo.redis.config.SpringRedisCacheConfig.CACHE_MANAGER;
import static com.github.zzf.learn.app.repo.redis.config.SpringRedisCacheConfig.TTL_5_MINUTES;
import static com.github.zzf.learn.app.repo.redis.config.SpringRedisCacheConfig.randomDuration;
import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.empty;
import static java.util.stream.Stream.ofNullable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.zzf.learn.app.common.ConfigService;
import com.github.zzf.learn.app.common.SearchAfter;
import com.github.zzf.learn.app.repo.redis.config.RedisConfig;
import com.github.zzf.learn.app.station.model.Station;
import com.github.zzf.learn.app.station.model.StationIdList;
import com.github.zzf.learn.app.station.repo.StationRepo;
import com.google.common.collect.Lists;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-03-08
 */
@Repository
@Slf4j
@Validated
@RequiredArgsConstructor
@CacheConfig(cacheManager = CACHE_MANAGER, cacheNames = CACHE_5_MINUTES)
@Primary
public class StationRepoRedisImpl implements StationRepo {

    final @Qualifier(BEAN_NAME) StationRepo delegate;
    final RedisTemplate<Long, Station> redisTemplate;
    final ConfigService configCenterService;
    final @Qualifier(ASYNC_THREAD) AsyncTaskExecutor executor;

    @Override
    public void save(Station station) {
        // delegate.save(station);
        // watch out: must use self.evictCache to trigger spring cache proxy
        self.evictCache(station.getId());
    }

    @Override
    public List<Station> queryBy(StationIdList idList) {
        // 前判断缓存穿透，idSet 代替 BF
        Set<Long> idSet = idList.getIdSet();
        if (configCenterService.isCheckStationIdExistsBeforeQuery()) {
            // timer("queryStationByIdList", stationIdList.size(), "stage", "init");
            Set<Long> existStationIdSet = self.queryList().stream().map(Station::getId).collect(toSet());
            idSet = idSet.stream().filter(existStationIdSet::contains).collect(toSet());
            if (idSet.isEmpty()) {
                return emptyList();
            }
        }
        // pipeline 一次可以支持 10K 个批量查询，一般远远超过业务请求数量
        // timer("queryStationByIdList", stationIdList.size(), "stage", "fetch");
        List<Station> cachedData = self.fetchFromCacheUsingMultiThread(idSet).toList();
        if (cachedData.size() == idSet.size()) { // all hit cache
            return cachedData;
        }
        Set<Long> cached = cachedData.stream().map(Station::getId).collect(toSet());
        Set<Long> missed = idSet.stream().filter(d -> !cached.contains(d)).collect(toSet());
        // timer("queryStationByIdList", missed.size(), "stage", "missed");
        // load from DB
        List<Station> dbData = delegate.queryBy(new StationIdList(missed));
        // 缓存穿透 cache penetration
        logCachePenetration(missed, dbData);
        // async pipeline set
        asyncUpdateCache(dbData);
        return Stream.concat(cachedData.stream(), dbData.stream()).collect(toList());
    }

    @Nullable
    @Override
    @Cacheable(key = "'station:' + #id", unless = "#result == null")
    public Station queryBy(Long id) {
        return delegate.queryBy(id);
    }

    @Override
    public List<Station> queryList() {
        return self.queryStationIndexCache().getList();
    }

    @Override
    public Iterator<List<Long>> iterator() {
        return delegate.iterator();
    }

    @Override
    public Page<Long> queryPageBy(Map<String, String> parameters, Pageable pageable) {
        return delegate.queryPageBy(parameters, pageable);
    }

    @Override
    public Stream<Station> searchAfter(SearchAfter req) {
        return delegate.searchAfter(req);
    }

    /**
     * 手动 evict
     * <pre>
     *     仅供手动删除
     *     仅供手动删除
     *     仅供手动删除
     *     业务代码中使用时需慎重
     *     业务代码中使用时需慎重
     *     业务代码中使用时需慎重
     * </pre>
     */
    @Caching(evict = {
        @CacheEvict(key = "'station:' + #stationId"),
        @CacheEvict(cacheManager = CAFFEINE_STATION, cacheNames = CAFFEINE_5_MINUTES, key = "'station:ids'")
    })
    public void evictCache(Long stationId) {
        log.info("evictCache -> stationId: {}", stationId);
    }

    private void asyncUpdateCache(List<Station> dbData) {
        executor.execute(() -> updateCache(dbData));
    }

    public Stream<Station> fetchFromCacheUsingMultiThread(Collection<Long> idList) {
        int size = configCenterService.getQueryStationByIdListCacheBatchSize();
        // 批量查询大部分场景下只有一个 key，切换到异步线程执行会增加不必要的线程切换
        if (idList.size() <= size) {
            return fetchFromCache(idList);
        }
        return Lists.partition(new ArrayList<>(idList), size).stream()
            // parallel query from db
            .map(list -> supplyAsync(() -> fetchFromCache(list), executor))
            // combine all the future task
            .reduce(completedFuture(empty()), (a, b) -> a.thenCombine(b, Stream::concat))
            // wait for task done
            .join();
    }

    private Stream<Station> fetchFromCache(Collection<Long> idList) {
        log.info("fetchFromCache -> idList: {}", idList);
        // lettuce pipeline 底层使用异步, 实测 lettuce key 可以跨 node
        List<Object> cacheList = redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public <K, V> Object execute(@Nonnull RedisOperations<K, V> operations) throws DataAccessException {
                idList.stream().map(id -> toRedisKey(id)).forEach(operations.opsForValue()::get);
                return null;
            }
        });
        return ofNullable(cacheList)
            .flatMap(Collection::stream)
            .map(d -> (Station) d)
            .filter(Objects::nonNull);
    }

    private void updateCache(List<Station> dbData) {
        if (dbData.isEmpty()) {
            return;
        }
        redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public Object execute(@Nonnull RedisOperations operations) throws DataAccessException {
                for (Station d : dbData) {
                    // randomDuration(TTL_5_MINUTES) 随机化缓存时间，避免缓存雪崩
                    operations.opsForValue().set(toRedisKey(d.getId()), d, randomDuration(TTL_5_MINUTES));
                }
                return null;
            }
        });
    }

    private void logCachePenetration(Collection<Long> idList, List<Station> dbData) {
        Set<Long> dbIdList = dbData.stream().map(Station::getId).collect(toSet());
        Set<Long> dbMissed = idList.stream().filter(id -> !dbIdList.contains(id)).collect(toSet());
        if (!dbMissed.isEmpty()) {
            // logEvent("Penetration", "Cache", dbMissed, "method", "queryStationByIdList");
        }
    }

    /**
     * <pre>
     *     性能考虑，内存缓存,5 minutes TTL
     * </pre>
     */
    @Cacheable(cacheManager = CAFFEINE_STATION, cacheNames = CAFFEINE_5_MINUTES, key = "'station:ids'")
    public StationIndexCache queryStationIndexCache() {
        return new StationIndexCache(delegate.queryList());
    }

    private String toRedisKey(Long id) {
        return CacheKeyPrefix.prefixed(APP_PREFIX).compute(CACHE_5_MINUTES) + "stations:" + id;
    }

    StationRepoRedisImpl self;

    @Autowired
    @Lazy
    public void setSelf(StationRepoRedisImpl self) {
        this.self = self;
    }

    @Data
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StationIndexCache {
        List<Station> list;
    }

    interface DomainMapper {
        DomainMapper INSTANCE = Mappers.getMapper(DomainMapper.class);
    }

    private static final String REDIS_TEMPLATE = "RedisTemplate_" + "StationRepoRedisImpl";

    /**
     * why？ 范性匹配
     */
    @SuppressWarnings("rawtypes")
    @Configuration
    public static class RedisTemplateAutowire {
        @Bean(REDIS_TEMPLATE)
        public RedisTemplate<Long, Station> redisTemplate(
            @Qualifier(RedisConfig.REDIS_TEMPLATE) RedisTemplate redisTemplate) {
            return redisTemplate;
        }
    }

}
