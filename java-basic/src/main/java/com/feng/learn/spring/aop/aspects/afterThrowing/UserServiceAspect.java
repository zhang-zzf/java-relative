package com.feng.learn.spring.aop.aspects.afterThrowing;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
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
public class UserServiceAspect {

  /**
   * AfterThrowing 运行时当检测到方法抛出异常时，执行相应的advice
   * <p><b>注意: 异常并不会被advice捕获，而是会继续向上抛出</b></p>
   */
  @AfterThrowing(value = "execution(* com..UserService.throwException(..))", throwing = "re")
  public void afterThrowingMethodOfUserService(RuntimeException re) {
    log.info("AfterThrowing: {}", re.getMessage());
  }

}
