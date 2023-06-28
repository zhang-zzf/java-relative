package com.github.learn.springself.aop.api.impl;

import com.github.learn.springself.aop.api.AopInterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@Service
@Slf4j
public class AopInterfaceServiceImpl implements AopInterfaceService {

  AopInterfaceServiceImpl self;

  @Override
  public void methodA() {
    self.methodB();
  }

  @Override
  public void methodB() {

  }

  @Autowired
  public void setSelf(AopInterfaceServiceImpl self) {
    this.self = self;
  }

}
