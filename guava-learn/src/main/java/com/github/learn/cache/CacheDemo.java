package com.github.learn.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * <pre>
 *   The first question to ask yourself about your cache is: is there some sensible default function to load or compute a value associated with a key? If so, you should use a CacheLoader. If not, or if you need to override the default, but you still want atomic "get-if-absent-compute" semantics, you should pass a Callable into a get call. Elements can be inserted directly, using Cache.put, but automatic cache loading is preferred as it makes it easier to reason about consistency across all cached content.
 * </pre>
 *
 * <pre>
 *   翻译过来：
 *   1. 若 Cache 溯源算法是固定的，优先使用 {@link LoadingCache}
 *   2. 若不能使用 {@link CacheLoader} 使用 {@link CacheDemo} 中的用法
 * </pre>
 */

@RequiredArgsConstructor
public class CacheDemo {

    private final DataSource dataSource;

    private final Cache<String, Integer> cache = CacheBuilder.newBuilder()
        .maximumSize(8)
        .build();

    private Integer retrieveFromSource(String key) {
        return dataSource.queryBy(key);
    }

    @SneakyThrows
    public Integer getByStringKey(String key) {
        return cache.get(key, () -> {
            // If the key wasn't in the "easy to compute" group, we need to
            // do things the hard way.
            return retrieveFromSource(key);
        });
    }

}

