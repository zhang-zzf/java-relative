package com.feng.learn.spring.aop.around;

import static org.assertj.core.api.Assertions.assertThat;

import com.feng.learn.spring.aop.aspects.around.UserServiceAspect;
import com.feng.learn.spring.aop.service.UserService;
import com.feng.learn.spring.aop.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/19
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    UserServiceImpl.class,
    UserServiceAspect.class,
})

public class UserServiceAspectTest {

  @Autowired
  UserService userService;

  @Test
  public void givenAroundAspect_whenCallMethod_thenThrowException() {
    assertThat(userService.save(0L, null)).isTrue();
  }
}
