package com.github.learn.id.generator.domain.service;

import com.github.learn.id.generator.domain.model.Sequence;
import com.github.learn.id.generator.domain.model.SequenceConfig;
import com.github.learn.id.generator.domain.repo.SequenceRepo;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class SequenceService {

  final SequenceRepo sequenceRepo;


  /**
   * 获取下一段 id 序列
   * <p>最少 128 个 id</p>
   * <p>根据当前 id 段用量实时调整下一段 id 的大小</p>
   *
   * @param cur            当前的序列
   * @param periodInSecond 下一段时长
   * @return data
   */
  public @NotNull Sequence getNextSequence(@NotNull Sequence cur, @Positive long periodInSecond) {
    Map<String, SequenceConfig> allConfig = sequenceRepo.getAllSequenceConfig();
    final SequenceConfig config = allConfig.get(cur.getKey());
    if (config == null) {
      log.warn("getNextSequence has no config: {}", cur);
      throw new IllegalArgumentException(cur.getKey() + " has no config in repo");
    }
    while (true) {
      long nextAvailableIdInRepo = config.getCurId();
      long usedIdCount = (cur.getNEXT_ID().get() - cur.getStartId()) / cur.getStep();
      long timeUsed = System.currentTimeMillis() - cur.getCreatedAt();
      // 兜底
      timeUsed = timeUsed == 0 ? 1 : timeUsed;
      long nextEightSecondMayNeed = usedIdCount * periodInSecond * 1000 / timeUsed;
      // 兜底
      nextEightSecondMayNeed = nextEightSecondMayNeed < 128 ? 128 : nextEightSecondMayNeed;
      long end = nextAvailableIdInRepo + nextEightSecondMayNeed * config.getStep();
      if (sequenceRepo.updateNextAvailableId(nextAvailableIdInRepo, end + config.getStep())) {
        return new Sequence(cur.getKey(), nextAvailableIdInRepo, end, cur.getStep());
      }
    }
  }


}
