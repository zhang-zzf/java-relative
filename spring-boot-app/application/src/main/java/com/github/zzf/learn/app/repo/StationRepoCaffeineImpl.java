package com.github.zzf.learn.app.repo;

import static com.github.zzf.learn.app.common.spring.async.ThreadPoolForStation.ASYNC_THREAD;
import static com.github.zzf.learn.app.repo.StationRepoMySQLImpl.BEAN_NAME;
import static java.util.stream.Collectors.toSet;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
// @Primary
public class StationRepoCaffeineImpl implements StationRepo {

    final @Qualifier(BEAN_NAME) StationRepo delegate;
    final @Qualifier(ASYNC_THREAD) AsyncTaskExecutor executor;
    // cache strategy: 5 minutes expire; 4 minute refresh
    // refresh 的逻辑: 4m 后，会触发一次 loadAll 的回调，重新回源获取数据，并更新缓存。
    // refresh 的逻辑: 更新完成之前，返回 cache 中现有的值
    // refresh 的逻辑: refresh 仅由 get / getAll 等查询触发。
    final String caffeineSpec = "maximumSize=16777216,expireAfterWrite=5m,refreshAfterWrite=4m";
    // stations -> 缓存全量门店
    final String STATION_ALL = "stations";
    final LoadingCache<String, List<Station>> stationCache = Caffeine.from(caffeineSpec)
        .executor(executor)
        .build(key -> delegate.queryList());

    @Override
    public void save(Station station) {
        // delegate.save(station);
        // watch out: must use self.evictCache to trigger spring cache proxy
        // 分布式缓存，使用 evictCache 没有意义
        // self.evictCache(station.getId());
    }

    @Override
    public List<Station> queryBy(StationIdList idList) {
        return stationCache.get(STATION_ALL).stream()
            .filter(d -> idList.getIdSet().contains(d.getId()))
            .toList();
    }

    @Nullable
    @Override
    public Station queryBy(Long id) {
        return stationCache.get(STATION_ALL).stream()
            .filter(d -> d.getId().equals(id))
            .findAny().orElse(null);
    }

    // 查询全部门店走 cache
    @Override
    public List<Station> queryList() {
        return stationCache.get(STATION_ALL);
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
        stationCache.invalidate(STATION_ALL);
    }

    private void logCachePenetration(Collection<Long> idList, List<Station> dbData) {
        Set<Long> dbIdList = dbData.stream().map(Station::getId).collect(toSet());
        Set<Long> dbMissed = idList.stream().filter(id -> !dbIdList.contains(id)).collect(toSet());
        if (!dbMissed.isEmpty()) {
            // logEvent("Penetration", "Cache", dbMissed, "method", "queryStationByIdList");
        }
    }

    StationRepoCaffeineImpl self;

    @Autowired
    @Lazy
    public void setSelf(StationRepoCaffeineImpl self) {
        this.self = self;
    }

}
