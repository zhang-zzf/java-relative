package com.feng.learn.spring.aop.aspects.composite;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/19
 */
@Aspect
@Configuration
@EnableAspectJAutoProxy
@Slf4j
@Order(-1)
public class UserServiceImplAspectBefore {

  @Before("execution(* com..UserServiceImpl.save(..))")
  public void beforeSaveOfUserServiceImplAspect(JoinPoint pjp) {
    log.info(
        "@Before: class => {}, method => {}\n@Before: this => {}, JointPoint.getThis => {}, target => {}",
        pjp.getSignature().getDeclaringType(), pjp.getSignature().getName(),
        Objects.hashCode(this), Objects.hashCode(pjp.getThis()), Objects.hashCode(pjp.getTarget()));
    // throw new IllegalArgumentException("beforeSaveOfUserServiceImplAspect");
  }

}
