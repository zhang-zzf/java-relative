package com.github.learn.spring_cycle_dependency.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/12
 */
@Service
@Slf4j
public class ServiceC {

  private ServiceC self;

  @Autowired
  @Lazy
  public void setSelf(ServiceC self) {
    this.self = self;
  }

  public void methodA() {
    log.info("{} methodA", Thread.currentThread().getName());
    self.methodB();
  }

  @Async
  public void methodB() {
    log.info("{} methodB", Thread.currentThread().getName());
  }

}
