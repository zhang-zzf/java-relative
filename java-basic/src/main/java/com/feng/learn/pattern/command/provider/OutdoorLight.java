package com.feng.learn.pattern.command.provider;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2019/12/31
 */
@Slf4j
public class OutdoorLight {

  public void on() {
    log.info("outdoorLight on");
  }

  public void off() {
    log.info("outdoorLight off");
  }
}
