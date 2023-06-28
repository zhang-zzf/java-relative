package com.github.learn.springself.aop.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@Service
@Slf4j
public class AopNoInterfaceService {

  AopNoInterfaceService self;

  public void methodA() {
    self.methodB();
  }

  void methodB() {
    log.info("methodB");
  }

  @Autowired
  public void setSelf(AopNoInterfaceService self) {
    this.self = self;
  }

}
