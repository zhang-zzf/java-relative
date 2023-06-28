package com.feng.learn.spring.config.cache.jvmredis;

import java.util.Collection;
import java.util.Collections;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.AbstractCacheManager;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/15
 */
public class LocalRemoteComposeCacheManager extends AbstractCacheManager {

  final CacheManager localCacheManage;
  final CacheManager remoteCacheManage;

  public LocalRemoteComposeCacheManager(CacheManager localCacheManage,
      CacheManager remoteCacheManage) {
    if (localCacheManage == null && remoteCacheManage == null) {
      throw new IllegalArgumentException();
    }
    this.localCacheManage = localCacheManage;
    this.remoteCacheManage = remoteCacheManage;
  }

  @Override
  protected Collection<? extends Cache> loadCaches() {
    return Collections.emptyList();
  }

  @Override
  protected Cache getMissingCache(String name) {
    Cache localCache = null, remoteCache = null;
    if (localCacheManage != null) {
      localCache = localCacheManage.getCache(name);
    }
    if (remoteCacheManage != null) {
      remoteCache = remoteCacheManage.getCache(name);
    }
    return new LocalRemoteComposeCache(name, localCache, remoteCache, true);
  }

}
