package com.feng.learn.aop.around;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import com.feng.learn.SpringAopApplication;
import com.feng.learn.service.UserService;
import com.feng.learn.util.Trace;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserServiceAspectTest {

  @Autowired
  UserService userService;

  @Test
  public void given_when_then() {
    // when
    boolean ret = userService.save(0L, "zhang.zzf");
    // then
    assertThat(ret).isTrue();
    List<String> trace = Trace.trace();
    log.info("trace -> {}", trace);
    then(trace.get(0)).contains("advice1").contains("@Before");
    then(trace.get(1)).contains("advice2").contains("@Before");
    then(trace.get(2)).contains("UserServiceImpl.save");
    then(trace.get(3)).contains("advice2").contains("@AfterReturn");
    then(trace.get(4)).contains("advice2").contains("@After");
    then(trace.get(5)).contains("advice1").contains("@AfterReturn");
    then(trace.get(6)).contains("advice1").contains("@After");

  }
}