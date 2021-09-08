package com.feng.learn.spring.aop.before;

import com.feng.learn.spring.aop.aspects.before.UserServiceImplAspect;
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
    UserServiceImplAspect.class,

})
public class UserServiceImplAspectTest {

    @Autowired
    UserService userService;

    @Test
    public void givenBeforeAdvice_whenCallSave_thenThrowException() {
        // when
        Throwable throwable = catchThrowable(() -> userService.save(0L, null));
        // then
        assertThat(throwable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("beforeSaveOfUserServiceImplAspect");

    }

}