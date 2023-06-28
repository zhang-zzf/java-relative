package com.feng.learn.spring.config.redis.lock;

import java.nio.charset.StandardCharsets;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ObjectUtils;

/**
 * 不可重入锁
 *
 * @author zhanfeng.zhang
 * @date 2019/11/26
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class RedisDistributedLock {

  @NonNull
  final RedisConnectionFactory redisConnectionFactory;

  final RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
  final RedisSerializer<String> keySerialized = stringRedisSerializer;
  final RedisSerializer<String> valueSerialized = keySerialized;
  final Converter<byte[], Boolean> okRespBooleanConverTer = s -> ObjectUtils.nullSafeEquals("OK",
      valueSerialized.deserialize(s));
  final byte[] NX = "NX".getBytes(StandardCharsets.UTF_8);
  final byte[] EX = "EX".getBytes(StandardCharsets.UTF_8);

  final byte[] UNLOCK_LUA = stringRedisSerializer.serialize("" +
      "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n"
      + "then\n"
      + "    return redis.call('del',KEYS[1])\n"
      + "else\n"
      + "    return 0\n"
      + "end"
  );


  /**
   * 加锁，一直等待直到获取到锁
   *
   * @param lockKey                 用于保护资源的redis-key
   * @param myRandomValue           必须是一个随机数，否者redis锁极有可能被其他人释放
   * @param expirationTimeInSeconds redis-key的自动过期时间，建议锁内业务逻辑的执行最大时间的4到16倍
   */
  public void lock(String lockKey, String myRandomValue, long expirationTimeInSeconds) {
    for (boolean locked = false; !locked; ) {
      locked = tryLock(lockKey, myRandomValue, expirationTimeInSeconds);
    }
  }

  /**
   * 尝试加锁，不管加锁是否成功，立即返回
   * <p>若加锁成功，<b>一定要释放锁</b></p>
   *
   * @param lockKey                 用于保护资源的redis-key
   * @param myRandomValue           必须是一个随机数，否者redis锁极有可能被其他人释放
   * @param expirationTimeInSeconds redis-key的自动过期时间，建议锁内业务逻辑的执行最大时间的4到16倍
   * @return true => 加锁成功；false => 加锁失败
   */
  public boolean tryLock(String lockKey, String myRandomValue, long expirationTimeInSeconds) {
    RedisConnection conn = RedisConnectionUtils.getConnection(redisConnectionFactory);
    try {
      // SET key my_random_value NX EX expirationTimeInSeconds
      byte[] resp = (byte[]) conn.execute("SET", new byte[][]{
          keySerialized.serialize(lockKey),
          valueSerialized.serialize(myRandomValue),
          NX,
          EX, stringRedisSerializer.serialize(String.valueOf(expirationTimeInSeconds))
      });
      return okRespBooleanConverTer.convert(resp);
    } finally {
      RedisConnectionUtils.releaseConnection(conn, redisConnectionFactory);
    }
  }

  /**
   * 解锁
   * <p><b>释放不是自己的锁/释放已经超时的锁(业务逻辑用时太长，redis-key已自动删除)</b> 后方法会抛出IllegalStateException，需要自己处理</p>
   *
   * @param lockKey       用于保护资源的redis-key
   * @param myRandomValue 必须是和用于lock的随机字符串相同，否者解锁失败会抛出异常
   * @throws IllegalStateException 解锁失败: 释放不是自己的锁/释放已经超时的锁(业务逻辑用时太长，redis-key已自动删除)
   */
  public void unlock(String lockKey, String myRandomValue) {
    RedisConnection conn = RedisConnectionUtils.getConnection(redisConnectionFactory);
    try {
      Long keyDeletedNum = conn.eval(UNLOCK_LUA, ReturnType.INTEGER, 1,
          keySerialized.serialize(lockKey),
          valueSerialized.serialize(myRandomValue));
      if (keyDeletedNum == 0) {
        throw new IllegalStateException(
            "the lock is not exists or has been expired, so your protected " +
                "resource may have been compromised.");
      }
    } finally {
      RedisConnectionUtils.releaseConnection(conn, redisConnectionFactory);
    }
  }

  public void lockInterruptibly(String lockKey, String myRandomValue, long expirationTimeInSeconds)
      throws InterruptedException {
    for (boolean locked = false; !locked; ) {
      // 响应线程中断
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      locked = tryLock(lockKey, myRandomValue, expirationTimeInSeconds);
    }
  }
}
