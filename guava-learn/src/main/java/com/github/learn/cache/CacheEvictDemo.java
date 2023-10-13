package com.github.learn.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class CacheEvictDemo {

  private final DataSource dataSource;

  /**
   * 最大容量
   */
  private final Cache<String, Integer> sizeBasedCache = CacheBuilder.newBuilder()
      .maximumSize(8)
      .build();

  private final Cache<String, Integer> timeBasedCache = CacheBuilder.newBuilder()
      // 写入后8分钟过期
      .expireAfterWrite(Duration.ofMinutes(8))
      // 4分钟没有访问过期
      .expireAfterAccess(Duration.ofMinutes(2))
      .build();

  @SneakyThrows
  public Integer queryFromTimeBasedCache(String key) {
    return timeBasedCache.get(key, key::hashCode);
  }


}

