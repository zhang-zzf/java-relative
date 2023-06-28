package com.github.learn.spring_cycle_dependency.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/11
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceB {

  private final ServiceA serviceA;

  public void methodA() {
    log.info("serviceB.methodA");
    serviceA.methodA();
  }


  public void methodB() {
    log.info("serviceB.methodB");
  }

}
