package com.feng.learn.spring.aop.aspects.composite;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/19
 */
@Aspect
@Configuration
@EnableAspectJAutoProxy
@Slf4j
public class UserServiceAspectAfterReturning {

  @AfterReturning(value = "execution(* com..UserService.save(..)) && args(id, name)",
      returning = "returnValue", argNames = "pjp,name,id,returnValue")
  public void afterReturningSaveMethodOfUserService1(JoinPoint pjp, final String name, long id,
      boolean returnValue) {
    log.info("@AfterReturning: class => {}, method => {}\n"
            + "@AfterReturning: this => {}, JointPoint.getThis => {}, target => {}",
        pjp.getSignature().getDeclaringType(), pjp.getSignature().getName(),
        Objects.hashCode(this), Objects.hashCode(pjp.getThis()), Objects.hashCode(pjp.getTarget()));

  }

  @AfterReturning(value = "execution(* com..UserService.save(..))", returning = "returnVal")
  public void afterReturningSaveMethodOfUserService2(JoinPoint pjp, boolean returnVal) {
    log.info("@AfterReturning: class => {}, method => {}\n"
            + "@AfterReturning: this => {}, JointPoint.getThis => {}, target => {}",
        pjp.getSignature().getDeclaringType(), pjp.getSignature().getName(),
        Objects.hashCode(this), Objects.hashCode(pjp.getThis()), Objects.hashCode(pjp.getTarget()));

    // throw new IllegalArgumentException("afterReturningSaveMethodOfUserService");
  }

}
