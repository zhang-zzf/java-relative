package com.feng.learn.pattern.command.provider;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2019/12/31
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Light {

  String lightLocation;

  public void on() {
    log.info("{}: light on", lightLocation);
  }

  public void off() {
    log.info("{}: light off", lightLocation);
  }
}
