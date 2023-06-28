package com.feng.learn.infrastructure.gray;

import com.feng.learn.infrastructure.gray.annotation.DegradeSwitch;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author zhanfeng.zhang
 * @date 2020/06/12
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@Aspect
@Slf4j
@RequiredArgsConstructor
public class SwitchAop {

  /**
   * "@DegradeSwitch" 切面逻辑
   */
  @Around("execution(* *(..)) && @annotation(degradeSwitch)")
  public Object functionGray(ProceedingJoinPoint pjp, DegradeSwitch degradeSwitch)
      throws Throwable {
    String configKey =
        pjp.getSignature().getDeclaringType().getSimpleName() + "#" + pjp.getSignature().getName();
    if (!("".equals(degradeSwitch.key()))) {
      configKey = degradeSwitch.key();
    }
    Object retVal;
    // 根据 configKey 判断灰度
    boolean degrade = true;
    if (degrade) {
      // log event
      log.info("{} occur degrade", configKey);
      Class<?>[] parameterTypes = ((MethodSignature) pjp.getSignature()).getMethod()
          .getParameterTypes();
      Method degradeToMethod = pjp.getTarget().getClass()
          .getDeclaredMethod(degradeSwitch.degradeTo(), parameterTypes);
      // 设置访问权限
      degradeToMethod.setAccessible(true);
      retVal = degradeToMethod.invoke(pjp.getTarget(), pjp.getArgs());
    } else {
      retVal = pjp.proceed();
    }
    return retVal;
  }
}
