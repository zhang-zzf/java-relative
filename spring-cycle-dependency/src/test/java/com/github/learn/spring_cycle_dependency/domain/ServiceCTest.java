package com.github.learn.spring_cycle_dependency.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/12
 */
@SpringBootTest
class ServiceCTest {

  @Autowired
  ServiceC serviceC;


  @Test
  void test() {
    serviceC.methodA();
  }

}