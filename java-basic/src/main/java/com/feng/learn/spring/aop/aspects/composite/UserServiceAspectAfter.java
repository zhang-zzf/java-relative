package com.feng.learn.spring.aop.aspects.composite;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/19
 */
@Aspect
@EnableAspectJAutoProxy
@Configuration
@Slf4j
public class UserServiceAspectAfter {

  @After(value = "execution(* com..UserService.save(..)) && args(id, name)", argNames = "pjp,id,name")
  public void afterSaveMethodOfUserService1(JoinPoint pjp, long id, String name) {
    log.info(
        "@After: class => {}, method => {}\n@After: this => {}, JointPoint.getThis => {}, target => {}",
        pjp.getSignature().getDeclaringType(), pjp.getSignature().getName(),
        Objects.hashCode(this), Objects.hashCode(pjp.getThis()), Objects.hashCode(pjp.getTarget()));

  }

  @After("execution(* com..UserService.save(..))")
  public void afterSaveMethodOfUserService2(JoinPoint pjp) {
    log.info(
        "@After: class => {}, method => {}\n@After: this => {}, JointPoint.getThis => {}, target => {}",
        pjp.getSignature().getDeclaringType(), pjp.getSignature().getName(),
        Objects.hashCode(this), Objects.hashCode(pjp.getThis()), Objects.hashCode(pjp.getTarget()));
  }

}
