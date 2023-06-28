package com.github.learn.id.generator.domain.service;

import com.github.learn.id.generator.domain.model.Sequence;
import com.github.learn.id.generator.infra.config.ConfigCenterService;
import com.github.learn.id.generator.infra.spring.async.SpringAsyncConfig;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author zhanfeng.zhang (zhang.zzf@alibaba-inc.com)
 * @date 2021/8/17
 */
@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class IdGeneratorService {

  private static final ConcurrentMap<String, Sequence> DATA = new ConcurrentHashMap<>(8);
  private static final ConcurrentMap<String, Future<Sequence>> EARLY_REQUIRE = new ConcurrentHashMap<>(
      8);
  final SequenceService sequenceService;
  @Qualifier(SpringAsyncConfig.ASYNC_THREAD)
  final AsyncTaskExecutor asyncTaskExecutor;
  final ConfigCenterService configCenterService;

  /**
   * get id
   * <p>语义：id 绝不重复，但不保证连续</p>
   *
   * @param key key
   * @return id
   */
  public @NotNull Long nextId(@NotNull String key) {
    if (DATA.get(key) == null) {
      synchronized (key.intern()) {
        if (DATA.get(key) == null) {
          Sequence nextSequence = getNextSequence(Sequence.initSequence(key));
          DATA.put(key, nextSequence);
        }
      }
    }
    final Optional<Long> nextId = DATA.get(key).nextId();
    if (nextId.isPresent()) {
      asyncTryEarlyGetNextSequence(DATA.get(key));
      return nextId.get();
    } else {
      Optional<Long> newNextId;
      synchronized (key.intern()) {
        Sequence nextSequence = tryFastGetNextSequence(key);
        if (nextSequence != null && (newNextId = nextSequence.nextId()).isPresent()) {
          DATA.put(key, nextSequence);
        } else {
          newNextId = DATA.get(key).nextId();
          if (!newNextId.isPresent()) {
            nextSequence = getNextSequence(DATA.get(key));
            DATA.put(key, nextSequence);
          }
        }
      }
      return newNextId.get();
    }
  }

  private void asyncTryEarlyGetNextSequence(Sequence cur) {
    if (cur.usedPercent() < configCenterService.getSequencePercentThreshold()) {
      return;
    }
    // 100 个并发，只会有一个能成功提交任务
    if (EARLY_REQUIRE.get(cur.getKey()) == null) {
      synchronized (cur.getKey().intern()) {
        if (EARLY_REQUIRE.get(cur.getKey()) == null) {
          Callable<Sequence> task = () -> getNextSequence(cur);
          Future<Sequence> future = asyncTaskExecutor.submit(task);
          EARLY_REQUIRE.put(cur.getKey(), future);
        }
      }
    }
  }

  @NotNull
  private Sequence getNextSequence(Sequence cur) {
    return sequenceService.getNextSequence(cur, 8);
  }

  private Sequence tryFastGetNextSequence(String key) {
    Future<Sequence> sequenceFuture = EARLY_REQUIRE.get(key);
    if (sequenceFuture != null && EARLY_REQUIRE.remove(key, sequenceFuture)) {
      try {
        return sequenceFuture.get();
      } catch (Exception e) {
        return null;
      }
    }
    return null;
  }

}
