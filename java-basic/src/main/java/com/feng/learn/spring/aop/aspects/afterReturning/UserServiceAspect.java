package com.feng.learn.spring.aop.aspects.afterReturning;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Objects;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/19
 */
@Aspect
@Configuration
@EnableAspectJAutoProxy
@Slf4j
public class UserServiceAspect {

    @AfterReturning(value = "execution(* com..UserService.save(..)) && args(id, name)",
        returning = "returnValue", argNames = "jp,name,id,returnValue")
    public void afterReturningSaveMethodOfUserService1(JoinPoint jp, final String name, long id, boolean returnValue) {
        log.info("AfterReturning: this=>{}, jp.getThis=>{}, target=>{}", Objects.hashCode(this),
            Objects.hashCode(jp.getThis()),
            Objects.hashCode(jp.getTarget()));
    }

    @AfterReturning(value = "execution(* com..UserService.save(..))", returning = "returnVal")
    public void afterReturningSaveMethodOfUserService2(JoinPoint jp, boolean returnVal) {
        log.info("@AfterReturning: this=>{}, jp.getThis=>{}, target=>{}", Objects.hashCode(this),
            Objects.hashCode(jp.getThis()),
            Objects.hashCode(jp.getTarget()));
        throw new IllegalArgumentException("afterReturningSaveMethodOfUserService");
    }

}
