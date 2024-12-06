package com.feng.learn.aop.order;

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
    UserServiceAspect1.class,
    UserServiceAspect0.class,
})
@Slf4j
public class UserServiceAspectTest {

    @Autowired
    UserService userService;

    @Test
    public void givenAspect_whenCallMethod_then() {
        // when
        userService.save(9L, "zzf");
        // then
        List<String> trace = Trace.trace();
        log.info("trace -> {}", trace);
        // 进入 0 圈
        then(trace.get(0)).contains("UserServiceAspect0").contains("around").contains("@Before");
        then(trace.get(1)).contains("UserServiceAspect0").contains("before");
        // 进入 1 圈
        then(trace.get(2)).contains("UserServiceAspect1").contains("around").contains("@Before");
        then(trace.get(3)).contains("UserServiceAspect1").contains("before");
        // 进入被代理类
        then(trace.get(4)).contains("UserServiceImpl.save");
        // 退出到 1 圈
        then(trace.get(5)).contains("UserServiceAspect1").contains("around").contains("@AfterReturn");
        then(trace.get(6)).contains("UserServiceAspect1").contains("around").contains("@After");
        then(trace.get(7)).contains("UserServiceAspect1").contains("after");
        then(trace.get(8)).contains("UserServiceAspect1").contains("afterReturn");
        // 退出到 0 圈
        then(trace.get(9)).contains("UserServiceAspect0").contains("around").contains("@AfterReturn");
        then(trace.get(10)).contains("UserServiceAspect0").contains("around").contains("@After");
        then(trace.get(11)).contains("UserServiceAspect0").contains("after");
        then(trace.get(12)).contains("UserServiceAspect0").contains("afterReturn");
    }
}
