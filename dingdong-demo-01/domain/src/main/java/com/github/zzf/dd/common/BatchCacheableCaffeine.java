package com.github.zzf.dd.common;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

@SuppressWarnings({"UnusedReturnValue", "NullableProblems", "unchecked", "unused"})
@Validated
public interface BatchCacheableCaffeine<ID, E> extends BatchCacheable<Collection<ID>, E> {

    Cache<String, E> caffeineCache();

    @Override
    default Stream<E> batchFetchCache(List<String> keyList) {
        return caffeineCache().getAllPresent(keyList).values().stream();
    }

    @Override
    default List<Object> batchUpdateCache(Map<String, E> keyToData, Duration ttl) {
        caffeineCache().putAll(keyToData);
        return emptyList();
    }

    @Override
    default void batchEvictCache(List<String> keyList) {
        caffeineCache().invalidateAll(keyList);
    }

}
