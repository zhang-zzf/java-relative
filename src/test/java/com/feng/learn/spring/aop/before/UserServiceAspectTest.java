package com.feng.learn.spring.aop.before;

import com.feng.learn.spring.aop.aspects.before.UserServiceAspect;
import com.feng.learn.spring.aop.service.UserService;
import com.feng.learn.spring.aop.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

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
    public void givenBeforeAspect_whenCallMethod_thenThrowException() {
        // when
        Throwable throwable = catchThrowable(() -> userService.get(0L));
        // then
        assertThat(throwable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("beforeEachUserServiceMethod");
        // when
        throwable = catchThrowable(() -> userService.save(0L, null));
        // then
        assertThat(throwable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("beforeEachUserServiceMethod");
    }
}
