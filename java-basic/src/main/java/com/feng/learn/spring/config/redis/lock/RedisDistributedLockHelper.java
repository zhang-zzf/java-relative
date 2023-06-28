package com.feng.learn.spring.config.redis.lock;

import java.util.UUID;
import lombok.RequiredArgsConstructor;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/26
 */
@RequiredArgsConstructor
public class RedisDistributedLockHelper {

  final RedisDistributedLock redisDistributedLock;

  final long redisKeyExpiredTime;

  /**
   * 获取redis锁 -> 执行task -> 释放redis锁
   * <p><b>业务逻辑用时大于expirationTimeInSeconds，redis-key被自动删除</b>
   * 后方法会抛出IllegalStateException，需要自己处理</p>
   *
   * @param task                    需要锁保护的业务逻辑，业务粒度越小越好
   * @param lockKey                 用于保护资源的redis-key
   * @param expirationTimeInSeconds redis-key的自动过期时间，建议task任务的执行最大时间的4到16倍
   * @throws IllegalStateException 业务逻辑用时 <b>大于</b> expirationTimeInSeconds
   */
  public void doInLock(Task task, String lockKey, long expirationTimeInSeconds) throws Exception {
    String randomStr = Thread.currentThread().getId() + "=>" + UUID.randomUUID().toString();
    redisDistributedLock.lock(lockKey, randomStr, expirationTimeInSeconds);
    try {
      task.doInLock();
    } finally {
      redisDistributedLock.unlock(lockKey, randomStr);
    }
  }

  /**
   * 获取redis锁 -> 执行task -> 释放redis锁
   * <p><b>业务逻辑用时大于默认的超时时间， redis-key被自动删除</b> 后方法会抛出IllegalStateException，需要自己处理</p>
   *
   * @param task    需要锁保护的业务逻辑，业务粒度越小越好
   * @param lockKey 用于保护资源的redis-key
   * @throws IllegalStateException 业务逻辑用时 <b>大于</b> 默认的redisKeyExpiredTime
   * @see #doInLock(Task, String, long)
   */
  public void doInLock(Task task, String lockKey) throws Exception {
    doInLock(task, lockKey, redisKeyExpiredTime);
  }

  /**
   * @throws IllegalStateException 业务逻辑用时 <b>大于</b> 默认的redisKeyExpiredTime
   * @see #doIfLocked(Task, String, long)
   */
  public void doIfLocked(Task task, String lockKey) throws Exception {
    doIfLocked(task, lockKey, redisKeyExpiredTime);
  }

  /**
   * same as:
   * <blockquote><pre>
   *     if (tryLock(lockKey, expirationTimeInSeconds)) {
   *         try {
   *             task.doInLock();
   *         } finally {
   *             unlock(lockKey);
   *         }
   *     }
   * </pre></blockquote>
   * <p><b>业务逻辑用时大于expirationTimeInSeconds，redis-key被自动删除</b>
   * 后方法会抛出IllegalStateException，需要自己处理</p>
   *
   * @throws IllegalStateException 业务逻辑用时 <b>大于</b> 默认的redisKeyExpiredTime
   * @see #doInLock(Task, String, long)
   */
  public void doIfLocked(Task task, String lockKey, long expirationTimeInSeconds) throws Exception {
    String randomStr = Thread.currentThread().getId() + "=>" + UUID.randomUUID().toString();
    boolean locked = redisDistributedLock.tryLock(lockKey, randomStr, expirationTimeInSeconds);
    if (!locked) {
      // 加锁失败，直接返回
      return;
    }
    try {
      task.doInLock();
    } finally {
      redisDistributedLock.unlock(lockKey, randomStr);
    }
  }

  @FunctionalInterface
  public interface Task {

    void doInLock() throws Exception;
  }
}
