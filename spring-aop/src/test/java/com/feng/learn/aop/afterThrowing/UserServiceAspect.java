package com.feng.learn.aop.afterThrowing;

import com.feng.learn.util.Trace;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author zhanfeng.zhang@icloud.com
 * @date 2024-09-03
 * <pre>
 *   同一个切面内多个 @AfterThrowing 的切入顺序和书写顺序无关
 *   <p>猜测和异常的继承关系有关，这个结论是错误的</p>
 *   1. runtimeExceptionAdvice
 *   2. throwableAdvice
 * </pre>
 * <pre>
 *   同一个切面内多个 advice 的切入顺序是不固定的，和书写顺序无关。
 *   <p>有些版本的实现: 执行的顺序与方法的名称有关</p>
 * </pre>
 */
@Aspect
@Component
@Slf4j
public class UserServiceAspect {

  /**
   * AfterThrowing 运行时当检测到方法抛出异常时，执行相应的advice
   * <p><b>注意: 异常并不会被advice捕获，而是会继续向上抛出</b></p>
   */
  @AfterThrowing(value = "execution(* com.feng.learn.service.UserService.throwException(..))", throwing = "th")
  public void aThrowableAdvice(Throwable th) {
    Trace.add(String.format("0x%016X", Objects.hashCode(this)));
    log.info("throwableAdvice: {}", th.getMessage());
  }

  /**
   * AfterThrowing 运行时当检测到方法抛出异常时，执行相应的advice
   * <p><b>注意: 异常并不会被advice捕获，而是会继续向上抛出</b></p>
   */
  @AfterThrowing(value = "execution(* com.feng.learn.service.UserService.throwException(..))", throwing = "re")
  public void runtimeExceptionAdvice(RuntimeException re) {
    Trace.add(String.format("0x%016X", Objects.hashCode(this)));
    log.info("runtimeExceptionAdvice: {}", re.getMessage());
  }

}
