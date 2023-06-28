package com.github.learn.springself.self;

import com.github.learn.springself.self.api.InterfaceService;
import com.github.learn.springself.self.domain.NoInterfaceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@SpringBootTest
class SelfTest {

  @Autowired
  InterfaceService interfaceService;
  @Autowired
  NoInterfaceService noInterfaceService;

  @Test
  void test() {
    interfaceService.methodA();
    noInterfaceService.methodA();
  }

}