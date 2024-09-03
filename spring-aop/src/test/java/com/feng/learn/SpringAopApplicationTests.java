package com.feng.learn;

import com.feng.learn.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAopApplicationTests {

  @Autowired
  UserService userService;

  @Test
  public void contextLoads() {
    userService.throwException(false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void contextLoads2() {
    userService.throwException(true);
  }
}
