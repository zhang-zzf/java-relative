package com.feng.learn.spring.aop.aspects.composite;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/19
 */
@Configuration
@Aspect
@EnableAspectJAutoProxy
@Slf4j
@Order(-11)
public class UserServiceAspectAround {

  @Around(value = "execution(* com..UserService.save(..)) && args(id, name)",
      argNames = "pjp, id, name")
  public Object aroundSaveMethodOfUserService(ProceedingJoinPoint pjp, long id, String name)
      throws Throwable {
    log.info(
        "@Around: class => {}, method => {}\n@Around: this => {}, JointPoint.getThis => {}, target => {}",
        pjp.getSignature().getDeclaringType(), pjp.getSignature().getName(),
        Objects.hashCode(this), Objects.hashCode(pjp.getThis()), Objects.hashCode(pjp.getTarget()));
    try {
      // @Before advice
      Object result = pjp.proceed();
      // @AfterReturn advice
      return result;
    } catch (Throwable throwable) {
      // do something after Throwable raised.
      // @AfterThrowing advice
      throw throwable;
    } finally {
      // @After advice
      // do something finally
    }
  }

}
