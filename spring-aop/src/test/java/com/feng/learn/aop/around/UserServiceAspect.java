package com.feng.learn.aop.around;

import com.feng.learn.util.Trace;
import java.util.Objects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
public class UserServiceAspect {

  @Around(value = "execution(* com.feng.learn.service.UserService.save(..)) && args(id, name)", argNames = "pjp, id, name")
  public Object advice2(ProceedingJoinPoint pjp, long id, String name) throws Throwable {
    try {
      Trace.add("@Before", String.format("0x%016X", Objects.hashCode(this)));
      // @Before advice
      Object result = pjp.proceed();
      Trace.add("@AfterReturn", String.format("0x%016X", Objects.hashCode(this)));
      // @AfterReturn advice
      return result;
    } catch (Throwable throwable) {
      Trace.add("@AfterThrowing", String.format("0x%016X", Objects.hashCode(this)));
      // do something after Throwable raised.
      // @AfterThrowing advice
      throw throwable;
    } finally {
      // @After advice
      Trace.add("@After", String.format("0x%016X", Objects.hashCode(this)));
      // do something finally
    }
  }

  @Around(value = "execution(* com.feng.learn.service.UserService.save(..)) && args(id, name)", argNames = "pjp, id, name")
  public Object advice1(ProceedingJoinPoint pjp, long id, String name) throws Throwable {
    try {
      Trace.add("@Before", String.format("0x%016X", Objects.hashCode(this)));
      // @Before advice
      Object result = pjp.proceed();
      // @AfterReturn advice
      Trace.add("@AfterReturn", String.format("0x%016X", Objects.hashCode(this)));
      return result;
    } catch (Throwable throwable) {
      Trace.add("@AfterThrowing", String.format("0x%016X", Objects.hashCode(this)));
      // do something after Throwable raised.
      // @AfterThrowing advice
      throw throwable;
    } finally {
      Trace.add("@After", String.format("0x%016X", Objects.hashCode(this)));
      // @After advice
      // do something finally
    }
  }
}
