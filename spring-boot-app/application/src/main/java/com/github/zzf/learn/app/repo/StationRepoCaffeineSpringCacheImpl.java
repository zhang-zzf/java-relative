package com.github.zzf.learn.app.repo;

import static com.github.zzf.learn.app.common.spring.cache.SpringCaffeineCacheConfig.CAFFEINE_5_MINUTES;
import static com.github.zzf.learn.app.common.spring.cache.SpringCaffeineCacheConfig.CAFFEINE_STATION;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.github.zzf.learn.app.common.ConfigService;
import com.github.zzf.learn.app.common.SearchAfter;
import com.github.zzf.learn.app.station.model.Station;
import com.github.zzf.learn.app.station.model.StationIdList;
import com.github.zzf.learn.app.station.repo.StationRepo;
import jakarta.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date 2025-05-20
 */
@Repository
@Slf4j
@Validated
@RequiredArgsConstructor
@CacheConfig(cacheManager = CAFFEINE_STATION, cacheNames = CAFFEINE_5_MINUTES)
// @Primary
public class StationRepoCaffeineSpringCacheImpl implements StationRepo {

    final @Qualifier(StationRepoMySQLImpl.BEAN_NAME) StationRepo delegate;
    final ConfigService configCenterService;

    @Override
    public void save(Station scmStation) {
        delegate.save(scmStation);
        // watch out: JVM caffeine cache, no need to refresh cache
        // self.refreshCache();
    }

    @Override
    public List<Station> queryBy(StationIdList stationIdList) {
        Set<Long> idSet = stationIdList.getIdSet();
        // 前判断缓存穿透，idSet 代替 BF
        if (configCenterService.isCheckStationIdExistsBeforeQuery()) {
            // timer("caffeine.queryStationByIdList", stationIdList.size(), "stage", "init");
            Set<Long> existStationIdSet = self.queryList().stream().map(Station::getId).collect(toSet());
            idSet = idSet.stream().filter(existStationIdSet::contains).collect(toSet());
            if (idSet.isEmpty()) {
                return emptyList();
            }
        }
        // timer("caffeine.queryStationByIdList", stationIdList.size(), "stage", "fetch");
        List<Station> cachedData = fetchFromCache(idSet).toList();
        if (cachedData.size() == idSet.size()) { // all hit cache
            return cachedData;
        }
        Set<Long> cached = cachedData.stream().map(Station::getId).collect(toSet());
        Set<Long> missed = idSet.stream().filter(d -> !cached.contains(d)).collect(toSet());
        // timer("caffeine.queryStationByIdList", missed.size(), "stage", "missed");
        // load from DB
        List<Station> dbData = delegate.queryBy(new StationIdList(missed));
        // 缓存穿透 cache penetration
        logCachePenetration(missed, dbData);
        asyncUpdateCache(dbData);
        return Stream.concat(cachedData.stream(), dbData.stream()).collect(toList());
    }

    private void asyncUpdateCache(List<Station> dbData) {
        if (dbData.isEmpty()) {
            return;
        }
        self.refreshCache();
    }

    private Stream<Station> fetchFromCache(Set<Long> stationIdList) {
        return self.queryList().stream().filter(d -> stationIdList.contains(d.getId()));
    }

    private void logCachePenetration(Collection<Long> idList, List<Station> dbData) {
        Set<Long> dbIdList = dbData.stream().map(Station::getId).collect(toSet());
        Set<Long> dbMissed = idList.stream().filter(id -> !dbIdList.contains(id)).collect(toSet());
        if (!dbMissed.isEmpty()) {
            // logEvent("Penetration", "Cache", dbMissed, "method", "caffeine.queryStationByIdList");
        }
    }

    /**
     * 4 分钟刷新1次缓存
     */
    @Scheduled(fixedDelay = 4 * 60 * 1000)
    public void scheduleRefreshCache() {
        log.info("scheduleRefreshCache -> start");
        self.refreshCache();
    }

    @CachePut(key = "'stations'")
    public List<Station> refreshCache() {
        List<Station> ret = queryAllStationListFromDelegate();
        log.info("refreshCache done -> updatedSize: {}", ret.size());
        return ret;
    }

    private List<Station> queryAllStationListFromDelegate() {
        return delegate.queryList();
    }

    @Nullable
    @Override
    public Station queryBy(Long id) {
        return self.queryList().stream()
            .filter(d -> d.getId().equals(id))
            .findAny().orElse(null);
    }

    // 查询全部门店走 cache
    @Override
    @Cacheable(key = "'stations'")
    public List<Station> queryList() {
        return delegate.queryList();
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
    public void evictCache(Long stationId) {
        log.info("evictCache -> stationId: {}", stationId);
    }

    StationRepoCaffeineSpringCacheImpl self;

    @Autowired
    @Lazy
    public void setSelf(StationRepoCaffeineSpringCacheImpl self) {
        this.self = self;
    }

}
