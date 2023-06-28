package com.github.learn.id.generator.domain.repo;

import com.github.learn.id.generator.domain.model.SequenceConfig;
import java.util.Map;

/**
 * @author zhanfeng.zhang (zhang.zzf@alibaba-inc.com)
 * @date 2021/8/17
 */
public interface SequenceRepo {

  /**
   * get next available id stored by id
   *
   * @param key key
   * @return data
   */
  long getNextAvailableId(String key);

  /**
   * update next available id
   * <p>cas Optimistic lock</p>
   *
   * @param expected 期望值
   * @param update   更新值
   * @return true 更新更新；false 更新失败
   */
  boolean updateNextAvailableId(long expected, long update);

  /**
   * 获取所有的 sequence 配置
   *
   * @return data
   */
  Map<String, SequenceConfig> getAllSequenceConfig();
}
