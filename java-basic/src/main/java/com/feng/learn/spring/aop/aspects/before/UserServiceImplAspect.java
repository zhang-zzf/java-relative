package com.feng.learn.spring.aop.aspects.before;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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
public class UserServiceImplAspect {

  @Before("execution(* com..UserServiceImpl.save(..))")
  public void beforeSaveOfUserServiceImplAspect() {
    throw new IllegalArgumentException("beforeSaveOfUserServiceImplAspect");
  }

}
