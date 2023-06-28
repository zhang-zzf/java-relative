package com.github.learn.id.generator.infra.config;

import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang (zhang.zzf@alibaba-inc.com)
 * @date 2021/8/18
 */
@Service
public class ConfigCenterService {

  public long getSequencePercentThreshold() {
    return 80;
  }

}
