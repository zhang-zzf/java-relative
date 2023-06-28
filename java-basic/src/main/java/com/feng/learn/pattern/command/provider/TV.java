package com.feng.learn.pattern.command.provider;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2019/12/31
 */
@Slf4j
public class TV {

  public void on() {
    log.info("TV on");
  }

  public void off() {
    log.info("TV off");
  }

  public void setInputChannel(int channel) {

  }

  public void setVolume(int volume) {

  }
}
