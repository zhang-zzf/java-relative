package com.feng.learn.spring.config.cache.jvmredis;

import java.util.concurrent.Callable;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.util.Assert;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/16
 */
public class LocalRemoteComposeCache extends AbstractValueAdaptingCache {

  private final String name;
  /**
   * local cache such as CaffeineCache
   */
  private final Cache localCache;
  /**
   * remote cache such as RedisCache...
   */
  private final Cache remoteCache;

  /**
   * Create an {@code AbstractValueAdaptingCache} with the given setting.
   *
   * @param allowNullValues whether to allow for {@code null} values
   */
  protected LocalRemoteComposeCache(String name, Cache localCache, Cache remoteCache,
      boolean allowNullValues) {
    super(allowNullValues);

    Assert.notNull(name, "Name must not be null!");
    if (localCache == null && remoteCache == null) {
      throw new IllegalArgumentException();
    }
    this.name = name;
    this.localCache = localCache;
    this.remoteCache = remoteCache;
  }

  private static <T> T valueFromLoader(Object key, Callable<T> valueLoader) {

    try {
      return valueLoader.call();
    } catch (Exception e) {
      throw new ValueRetrievalException(key, valueLoader, e);
    }
  }

  @Override
  protected Object lookup(Object key) {
    ValueWrapper val = null;
    if (localCache != null) {
      // first lookup local cache
      val = localCache.get(key);
    }
    if (val == null && remoteCache != null) {
      val = remoteCache.get(key);
    }
    return val == null ? null : val.get();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Object getNativeCache() {
    return null;
  }

  @Override
  public <T> T get(Object key, Callable<T> valueLoader) {
    ValueWrapper result = get(key);
    if (result != null) {
      return (T) result.get();
    }
    T value = valueFromLoader(key, valueLoader);
    put(key, value);
    return value;
  }

  @Override
  public void put(Object key, Object value) {
    if (localCache != null) {
      localCache.put(key, value);
    }
    if (remoteCache != null) {
      remoteCache.put(key, value);
    }
  }

  @Override
  public void evict(Object key) {
    if (localCache != null) {
      localCache.evict(key);
    }
    if (remoteCache != null) {
      remoteCache.evict(key);
    }
  }

  @Override
  public void clear() {
    if (localCache != null) {
      localCache.clear();
    }
    if (remoteCache != null) {
      remoteCache.clear();
    }

  }
}
