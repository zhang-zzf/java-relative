package com.feng.learn.spring.aop.composite;

import static org.assertj.core.api.Assertions.assertThat;

import com.feng.learn.spring.aop.service.UserService;
import com.feng.learn.spring.aop.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/19
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    UserServiceImpl.class,
    UserServiceAspectTest.AspectsContext.class,
})
@Slf4j
public class UserServiceAspectTest {

  @Autowired
  UserService userService;

  @Test
  public void givenAroundAspect_whenCallMethod_thenThrowException() {
    assertThat(userService.save(0L, null)).isTrue();
  }

  @Configuration
  @ComponentScan("com.feng.learn.spring.aop.aspects.composite")
  static class AspectsContext {

  }

}
