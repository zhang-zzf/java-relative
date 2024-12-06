package com.feng.learn.aop.after;

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
 * @author zhanfeng.zhang
 * @date 2019/11/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringAopApplication.class)
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
        userService.save(0L, "zhang.zzf");
        List<String> trace = Trace.trace();
        // then
        then(trace.get(0)).contains("com.feng.learn.service.impl.UserServiceImpl.save");
        then(trace.get(1)).contains("com.feng.learn.aop.after.UserServiceAspect.advice1");
        then(trace.get(2)).contains("com.feng.learn.aop.after.UserServiceAspect.advice2");
        log.info("trace -> {}", trace);
    }
}
