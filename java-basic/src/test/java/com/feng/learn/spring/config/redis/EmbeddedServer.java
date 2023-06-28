package com.feng.learn.spring.config.redis;

import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisExecProvider;
import redis.embedded.RedisServer;
import redis.embedded.util.Architecture;
import redis.embedded.util.OS;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/16
 */
@Configuration
public class EmbeddedServer {

  public static final String EMBEDDED_REDIS_SERVER_PATH = "redis-server@3.2";
  public static final int EMBEDDED_REDIS_SERVER_PORT = 6379;
  public static final String EMBEDDED_REDIS_SERVER_HOST = "127.0.0.1";

  /**
   * embedded redis server
   */
  @Bean(initMethod = "start", destroyMethod = "stop")
  public RedisServer redisServer() throws IOException {
    return RedisServer.builder()
        .redisExecProvider(RedisExecProvider.defaultProvider()
            .override(OS.MAC_OS_X, Architecture.x86_64, EMBEDDED_REDIS_SERVER_PATH))
        .port(EMBEDDED_REDIS_SERVER_PORT)
        .setting("bind " + EMBEDDED_REDIS_SERVER_HOST)
        .setting("daemonize no")
        .setting("appendonly no")
        .setting("maxmemory 128M")
        /**
         * # volatile-lru -> Evict using approximated LRU among the keys with an expire set.
         *    1 # allkeys-lru -> Evict any key using approximated LRU.
         *    2 # volatile-lfu -> Evict using approximated LFU among the keys with an expire set.
         *    3 # allkeys-lfu -> Evict any key using approximated LFU.
         *    4 # volatile-random -> Remove a random key among the ones with an expire set.
         *    5 # allkeys-random -> Remove a random key, any key.
         *    6 # volatile-ttl -> Remove the key with the nearest expire time (minor TTL)
         *    7 # noeviction -> Don't evict anything, just return an error on write operations.
         */
        .setting("maxmemory-policy noeviction")
        .build();
  }

}
