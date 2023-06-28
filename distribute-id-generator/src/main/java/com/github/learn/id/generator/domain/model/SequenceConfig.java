package com.github.learn.id.generator.domain.model;

import java.util.Objects;
import lombok.Data;

/**
 * @author zhanfeng.zhang (zhang.zzf@alibaba-inc.com)
 * @date 2021/8/18
 */
@Data
public class SequenceConfig {

  private String key;
  private long startId;
  private int step;
  private long curId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SequenceConfig that = (SequenceConfig) o;
    return key.equals(that.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key);
  }
}
