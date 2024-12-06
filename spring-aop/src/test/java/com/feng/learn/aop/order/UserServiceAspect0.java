package com.feng.learn.aop.order;

import com.feng.learn.util.Trace;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;

/**
 * @author zhanfeng.zhang@icloud.com
 * @date 2024-09-03
 *
 * <pre>
 *   Advice列表的添加顺序是按照
 *   1. Around
 *   1. Before
 *   1. After
 *   1. AfterReturning
 *   1. AfterThrowing
 * </pre>
 */
@Aspect
@EnableAspectJAutoProxy
@Configuration
@Slf4j
@Order(0)
public class UserServiceAspect0 {

    @After(value = "execution(* com..UserService.save(..)) && args(id, name)", argNames = "pjp,id,name")
    public void after(JoinPoint pjp, long id, String name) {
        Trace.add(String.format("0x%016X", Objects.hashCode(this)));
    }

    @AfterReturning(value = "execution(* com..UserService.save(..)) && args(id, name)", returning = "ret", argNames = "pjp,name,id,ret")
    public void afterReturning(JoinPoint pjp, final String name, long id, boolean ret) {
        Trace.add(String.format("0x%016X", Objects.hashCode(this)));
    }

    /**
     * AfterThrowing 运行时当检测到方法抛出异常时，执行相应的advice
     * <p><b>注意: 异常并不会被advice捕获，而是会继续向上抛出</b></p>
     */
    @AfterThrowing(value = "execution(* com..UserService.save(..))", throwing = "re")
    public void afterThrowing(JoinPoint pjp, RuntimeException re) {
        Trace.add(String.format("0x%016X", Objects.hashCode(this)));
    }

    @Around(value = "execution(* com.feng.learn.service.UserService.save(..)) && args(id, name)", argNames = "pjp, id, name")
    public Object around(ProceedingJoinPoint pjp, long id, String name) throws Throwable {
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

    @Before("execution(* com.feng.learn.service.UserService.*(..))")
    public void before() {
        Trace.add(String.format("0x%016X", Objects.hashCode(this)));
    }

}
