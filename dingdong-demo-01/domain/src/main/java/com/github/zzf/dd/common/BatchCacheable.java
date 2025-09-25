package com.github.zzf.dd.common;

import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.github.zzf.dd.utils.MetricsUtil.logEvent;
import static com.github.zzf.dd.utils.MetricsUtil.timer;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;

@SuppressWarnings({"UnusedReturnValue", "unchecked", "unused"})
@Validated
public interface BatchCacheable<ID, E> extends BatchQueryOfList {

    Duration TTL_5_MINUTES = Duration.ofMinutes(5);

    Stream<E> batchFetchCache(List<String> keyList);

    List<Object> batchUpdateCache(Map<String, E> keyToData, Duration ttl);

    void batchEvictCache(List<String> keyList);

    default List<E> batchCacheable(@NotNull Set<ID> idSet,
                                   @NotNull Function<ID, String> toCacheKeyFunc,
                                   @NotNull Function<Set<ID>, List<E>> sourceFunc,
                                   @NotNull Executor executor,
                                   @Max(8192) int batchSize) {
        return batchCacheable(idSet, toCacheKeyFunc, getIdFunc(), sourceFunc, executor, batchSize);
    }

    default List<E> batchCacheable(@NotNull Set<ID> idSet,
                                   @NotNull Function<ID, String> toCacheKeyFunc,
                                   @NotNull Function<Set<ID>, List<E>> sourceFunc) {
        return batchCacheable(idSet, toCacheKeyFunc, getIdFunc(), sourceFunc);
    }

    @SuppressWarnings("unchecked")
    private Function<E, ID> getIdFunc() {
        return (e) -> {
            try {
                return (ID) e.getClass().getDeclaredMethod("getId").invoke(e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    default List<E> batchCacheable(@NotNull Collection<ID> idSet,
                                   @NotNull Function<ID, String> toCacheKeyFunc,
                                   @NotNull Function<E, ID> entityIdFunc,
                                   @NotNull Function<Set<ID>, List<E>> sourceFunc) {
        return batchCacheable(idSet, toCacheKeyFunc, entityIdFunc, sourceFunc, 400);
    }

    default List<E> batchCacheable(@NotNull Collection<ID> idSet,
                                   @NotNull Function<ID, String> toCacheKeyFunc,
                                   @NotNull Function<E, ID> entityIdFunc,
                                   @NotNull Function<Set<ID>, List<E>> sourceFunc,
                                   @Max(8192) int batchSize) {
        return batchCacheable(idSet, toCacheKeyFunc, entityIdFunc, sourceFunc, null, batchSize);
    }

    default List<E> batchCacheable(@NotNull Collection<ID> idSet,
                                   @NotNull Function<ID, String> toCacheKeyFunc,
                                   @NotNull Function<E, ID> entityIdFunc,
                                   @NotNull Function<Set<ID>, List<E>> sourceFunc,
                                   @Nullable Executor executor,
                                   @Max(8192) int batchSize) {
        return batchCacheable(idSet,
                toCacheKeyFunc,
                entityIdFunc,
                sourceFunc,
                executor,
                batchSize,
                TTL_5_MINUTES);
    }

    default void batchCacheEvict(@NotNull Set<ID> idToDel,
                                 @NotNull Function<ID, String> toCacheKeyFunc,
                                 @Nullable Executor executor) {
        if (idToDel.isEmpty()) {
            return;
        }
        List<String> keysToDel = idToDel.stream().map(toCacheKeyFunc).collect(toList());
        Runnable task = () -> batchEvictCache(keysToDel);
        if (executor != null) {
            executor.execute(task);
        } else {
            task.run();
        }
    }

    default List<E> batchCacheable(@NotNull Collection<ID> idSet,
                                   @NotNull Function<ID, String> toCacheKeyFunc,
                                   @NotNull Function<E, ID> entityIdFunc,
                                   @NotNull Function<Set<ID>, List<E>> sourceFunc,
                                   @Nullable Executor executor,
                                   @Max(8192) int batchSize,
                                   @NotNull Duration ttl
    ) {
        if (idSet.isEmpty()) {
            return emptyList();
        }
        timer("batchCacheable", idSet.size(), "clazz", clazz(), "stage", "query");
        // fetch from cache
        List<E> cachedData = fetchFromCache(idSet, toCacheKeyFunc, executor, batchSize);
        if (cachedData.size() == idSet.size()) {// all hit cache
            return cachedData;
        }
        // miss cache, get from db
        Set<ID> cachedIdSet = cachedData.stream().map(entityIdFunc).collect(toSet());
        Set<ID> missedIdSet = idSet.stream().filter(id -> !cachedIdSet.contains(id)).collect(toSet());
        timer("batchCacheable", idSet.size(), "clazz", clazz(), "stage", "missed");
        List<E> dbData = sourceFunc.apply(missedIdSet);
        // update cache
        updateCache(dbData, entityIdFunc, toCacheKeyFunc, executor, ttl);
        // 缓存穿透 cache penetration
        logCachePenetration(missedIdSet, dbData, entityIdFunc);
        // merge and return
        return Stream.concat(cachedData.stream(), dbData.stream()).collect(toList());
    }

    private void logCachePenetration(Set<ID> ids, List<E> dbData, Function<E, ID> entityIdFunc) {
        Set<ID> dbIds = dbData.stream().map(entityIdFunc).collect(toSet());
        Set<ID> dbMissed = ids.stream().filter(id -> !dbIds.contains(id)).collect(toSet());
        if (!dbMissed.isEmpty()) {
            logEvent("Penetration", "Cache", dbMissed, "clazz", clazz());
        }
    }

    private List<E> fetchFromCache(Collection<ID> idSet,
                                   Function<ID, String> toCacheKey,
                                   Executor executor,
                                   int batchSize) {
        if (idSet == null || idSet.isEmpty()) {
            return emptyList();
        }
        return batchQuery(idSet, (keys) -> fetchFromCache(keys, toCacheKey), batchSize, executor)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private Stream<E> fetchFromCache(List<ID> idSet, Function<ID, String> toCacheKey) {
        return batchFetchCache(idSet.stream().map(toCacheKey).collect(toList()));
    }

    private void updateCache(List<E> dbData,
                             Function<E, ID> entityId,
                             Function<ID, String> toCacheKey,
                             Executor executor,
                             Duration ttl) {
        if (dbData.isEmpty()) {
            return;
        }
        if (executor == null) {
            doUpdateCache(dbData, entityId, toCacheKey, ttl);
        } else {
            executor.execute(() -> doUpdateCache(dbData, entityId, toCacheKey, ttl));
        }
    }

    private List<Object> doUpdateCache(List<E> dbData,
                                       Function<E, ID> entityId,
                                       Function<ID, String> toCacheKey,
                                       Duration ttl) {
        Map<String, E> keyToData = dbData.stream()
                .collect(toMap(d -> toCacheKey.apply(entityId.apply(d)), d -> d));
        return batchUpdateCache(keyToData, ttl);
    }

    private String clazz() {
        return this.getClass().getSimpleName();
    }

    default Duration randomDuration(@NotNull Duration d) {
        return Duration.ofMillis((long) (d.toMillis() * ThreadLocalRandom.current().nextDouble(1.0, 1.1)));
    }

}
