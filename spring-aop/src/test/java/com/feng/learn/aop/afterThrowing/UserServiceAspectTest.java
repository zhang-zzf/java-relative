package com.feng.learn.aop.afterThrowing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.feng.learn.SpringAopApplication;
import com.feng.learn.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhanfeng.zhang@icloud.com
 * @date 2024-09-03
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringAopApplication.class})
@ContextConfiguration(classes = {
    UserServiceAspect.class,
})
public class UserServiceAspectTest {

  @Autowired
  UserService userService;

  @Test
  public void given_when_then() {
    // when
    Throwable throwable = catchThrowable(() -> userService.throwException(true));
    // then
    assertThat(throwable)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("userServiceImpl");
  }

  @Test(expected = IllegalArgumentException.class)
  public void givenAfterAspect_whenCallMethod_thenThrowException2() {
    userService.throwException(true);
  }
}
