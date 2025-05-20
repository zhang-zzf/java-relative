package com.github.zzf.learn.app.common.spring.cache;

import static com.github.zzf.learn.app.utils.LogUtils.json;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.zzf.learn.app.common.spring.async.SpringAsyncConfig;
import com.github.zzf.learn.app.station.model.Station;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-05-19
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class CaffeineCacheTest {

    public static final int DB_MILLIS = 100;
    @Mock
    Function<Object, Object> fetchDataFromDb;

    @Test
    void givenCaffeineCache_whenQuery_thenHitCache() {
        String caffeineSpec = "maximumSize=16777216,expireAfterWrite=5m";
        // CaffeineSpec spec = CaffeineSpec.parse(caffeineSpec);
        Cache<Object, Object> cache = Caffeine.from(caffeineSpec).build();
        // miss -> fetchDataFromDb -> put in cache
        then(cache.get("key_1", this::fetchDataFromDb)).isNotNull();
        // hit cache
        then(cache.get("key_1", this::fetchDataFromDb)).isNotNull();
        // Remove an entry
        cache.invalidate("key_1");
    }

    /**
     * 验证缓存回源逻辑只被调用1次
     */
    @Test
    void givenCaffeineCache_whenQuery_thenFetchDataFromDbOnce() {
        String caffeineSpec = "maximumSize=16777216,expireAfterWrite=5m";
        // CaffeineSpec spec = CaffeineSpec.parse(caffeineSpec);
        Cache<Object, Object> cache = Caffeine.from(caffeineSpec).build();
        // given
        given(fetchDataFromDb.apply(any())).willReturn(new Station());
        then(cache.get("key_1", fetchDataFromDb)).isNotNull();
        then(cache.get("key_1", fetchDataFromDb)).isNotNull();
        // 缓存回源逻辑只被调用1次
        BDDMockito.then(fetchDataFromDb).should(times(1)).apply(any());
    }

    // 缓存回源逻辑
    private String fetchDataFromDb(Object key) {
        return "value_1";
    }

    /**
     * 不推荐业务流程中手动更新缓存
     */
    @Test
    void givenCaffeineCache2_whenQuery_thenHitCache() {
        String caffeineSpec = "maximumSize=16777216,expireAfterWrite=5m";
        // CaffeineSpec spec = CaffeineSpec.parse(caffeineSpec);
        Cache<Object, Object> cache = Caffeine.from(caffeineSpec).build();
        then(cache.getIfPresent("key_1")).isNull();
        // 手动写入缓存
        cache.put("key_1", "value_1");
        then(cache.getIfPresent("key_1")).isNotNull();
    }

    /**
     * 缓存不存在时，自动加载缓存
     */
    @Test
    void givenCaffeineCacheAutoLoading_whenQuery_thenHitCache() {
        String caffeineSpec = "maximumSize=16777216,expireAfterWrite=5m";
        LoadingCache<Long, Station> cache = Caffeine.from(caffeineSpec)
            .build(this::loadStationFromDb);
        then(cache.get(1L)).isNull();
        then(cache.get(11L)).isNotNull();
    }

    /**
     * 异步缓存
     */
    @SneakyThrows
    @Test
    void givenCaffeineAsync_whenQuery_thenHitCache() {
        ThreadPoolTaskExecutor executor = new SpringAsyncConfig().taskExecutor();
        // not spring context, so need init manually
        executor.afterPropertiesSet();
        String caffeineSpec = "maximumSize=16777216,expireAfterWrite=5m";
        AsyncCache<Long, Station> cache = Caffeine.from(caffeineSpec)
            .executor(executor)
            .buildAsync();
        then(cache.getIfPresent(11L)).isNull();
        CompletableFuture<Station> future = cache.get(11L,
            (key, exec) -> supplyAsync(() -> loadStationFromDb(key), exec));
        // 打印日志在 executor 线程执行
        future.thenAccept(station -> log.info("station: {}", json(station))).join();
        log.info("givenCaffeineAsync_whenQuery_thenHitCache done");
    }

    /**
     * 自动刷新，默认配置下，没有调度更新；只有访问时发现缓存已过期，则触发刷新
     * <pre>
     *     Refreshing is not quite the same as eviction.
     *     As specified in LoadingCache.refresh(K), refreshing a key loads a new value for the key asynchronously.
     *     The old value (if any) is still returned while the key is being refreshed, in contrast to eviction, which forces retrievals to wait until the value is loaded anew
     * </pre>
     * <pre>
     *     In contrast to expireAfterWrite, refreshAfterWrite will make a key eligible for refresh after the specified duration,
     *     but a refresh will only be actually initiated when the entry is queried.
     *     So, for example, you can specify both refreshAfterWrite and expireAfterWrite on the same cache so that the expiration timer on an entry isn't blindly reset whenever an entry becomes eligible for a refresh.
     *     If an entry isn't queried after it comes eligible for refreshing, it is allowed to expire
     *
     *     与expireAfterWrite（写入后过期）不同，refreshAfterWrite（写入后刷新）会使键在指定持续时间后具备刷新资格，但只有当条目被查询时才会实际触发刷新。例如，你可以在同一个缓存中同时指定refreshAfterWrite和expireAfterWrite，这样当条目具备刷新资格时，其过期计时器不会被盲目重置。如果某个条目在具备刷新资格后未被查询，则允许其过期
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenCaffeineRefreshAfterWriter_whenQuery_thenHitCache() {
        ThreadPoolTaskExecutor executor = new SpringAsyncConfig().taskExecutor();
        // not spring context, so need init manually
        executor.afterPropertiesSet();
        String caffeineSpec = "maximumSize=16777216,expireAfterWrite=5m";
        LoadingCache<Long, Station> cache = Caffeine.from(caffeineSpec)
            .refreshAfterWrite(Duration.ofSeconds(1)) // 3S 刷新1次
            .executor(executor)
            .build(this::loadStationFromDb);
        Station station = cache.get(11L);
        then(station).isNotNull();
        // manual debug
        Thread.sleep(3000L);
    }

    @Spy
    CacheLoader<Long, Station> stationCacheLoader = new CacheLoader<Long, Station>() {
        @Override
        public @Nullable Station load(Long key) throws Exception {
            return loadStationFromDb(key);
        }
    };

    /**
     * <pre>
     *      自动刷新，默认配置下，没有调度更新；只有访问时发现缓存已过期，则触发刷新
     *      refreshAfterWrite 触发的 CacheLoader 由 executor 线程执行
     *
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenCaffeineRefreshAfterWriter_whenQuery_then2() {
        ThreadPoolTaskExecutor executor = new SpringAsyncConfig().taskExecutor();
        // not spring context, so need init manually
        executor.afterPropertiesSet();
        int refreshMillis = 100;
        LoadingCache<Long, Station> cache = Caffeine.from("maximumSize=16777216,expireAfterWrite=5m")
            .refreshAfterWrite(Duration.ofMillis(refreshMillis))
            .executor(executor)
            .build(stationCacheLoader);
        // 第1次 触发 CacheLoader
        then(cache.get(11L)).isNotNull();
        // 第2次
        then(cache.get(11L)).isNotNull();
        Thread.sleep(2 * refreshMillis + DB_MILLIS);
        // 第3次
        // 再次访问，触发 CacheLoader
        then(cache.get(11L)).isNotNull();
        Thread.sleep(2 * refreshMillis + DB_MILLIS);
        // 第4次
        // 再次访问，触发 CacheLoader
        then(cache.get(11L)).isNotNull();
        Thread.sleep(1000L);
        BDDMockito.then(stationCacheLoader).should(times(3)).load(any());
    }

    @SneakyThrows
    private @Nullable Station loadStationFromDb(Long key) {
        if (key < 10L) {
            return null;
        }
        Thread.sleep(DB_MILLIS);
        log.info("loadStationFromDb -> {}", key);
        return new Station();
    }


}