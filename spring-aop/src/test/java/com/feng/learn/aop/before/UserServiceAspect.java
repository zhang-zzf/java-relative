package com.feng.learn.aop.before;

import com.feng.learn.util.Trace;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


/**
 * @author zhanfeng.zhang@icloud.com
 * @date 2024-09-03
 *
 * <pre>
 *   同一个切面内多个 advice 的切入顺序是不固定的，和书写顺序无关。
 *   <p>有些版本的实现: 执行的顺序与方法的名称有关</p>
 * </pre>
 */
@Aspect
@Component
@Slf4j
public class UserServiceAspect {

  @Before("execution(* com.feng.learn.service.UserService.*(..))")
  public void advice2() {
    Trace.add(String.format("0x%016X", Objects.hashCode(this)));
  }

  @Before("execution(* com.feng.learn.service.impl.UserServiceImpl.save(..))")
  public void advice1() {
    Trace.add(String.format("0x%016X", Objects.hashCode(this)));
  }

}
