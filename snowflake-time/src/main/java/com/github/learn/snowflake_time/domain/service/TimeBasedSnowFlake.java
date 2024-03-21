package com.github.learn.snowflake_time.domain.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/18
 */
@Service
@Slf4j
public class TimeBasedSnowFlake {

  public static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

  /**
   * generate id use time
   * <p>不保证 id 不重复</p>
   * <p>${yyyyMMddHHmmss} * 100000 + ${ms} + new Random().nextInt(99000)</p>
   * <p>sample: 20210818 220256 6288</p>
   *
   * @return id
   */
  public long generate() {
    final String time = YYYY_MM_DD_HH_MM_SS.format(LocalDateTime.now());
    return Long.parseLong(time) * 100000 + ThreadLocalRandom.current().nextInt(99999);
  }

}
