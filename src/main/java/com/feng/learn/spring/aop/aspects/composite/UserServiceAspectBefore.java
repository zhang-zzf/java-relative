package com.feng.learn.spring.aop.aspects.composite;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;

import java.util.Objects;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/19
 */
@Aspect
@Configuration
@EnableAspectJAutoProxy
@Slf4j
@Order(2)
public class UserServiceAspectBefore {

    /**
     * 面向接口的切面
     * UserService 所有的方法前置切面
     */
    @Before("execution(* com..UserService.*(..))")
    public void beforeEachUserServiceMethod(JoinPoint pjp) {
        log.info("@Before: class => {}, method => {}\n@Before: this => {}, JointPoint.getThis => {}, target => {}",
            pjp.getSignature().getDeclaringType(), pjp.getSignature().getName(),
            Objects.hashCode(this), Objects.hashCode(pjp.getThis()), Objects.hashCode(pjp.getTarget()));
        // throw new IllegalArgumentException("beforeEachUserServiceMethod");
    }

}
