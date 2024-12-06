package com.feng.learn.aop.afterReturning;

import com.feng.learn.util.Trace;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author zhanfeng.zhang@icloud.com
 * @date 2024-09-03
 * <pre>
 *   同一个切面内多个 advice 的切入顺序是不固定的，和书写顺序无关。
 *   <p>有些版本的实现: 执行的顺序与方法的名称有关</p>
 * </pre>
 */
@Aspect
@Component
@Slf4j
public class UserServiceAspect {

    @Pointcut(value = "execution(* com.feng.learn.service.UserService.save(..)) && args(id, name)", argNames = "id,name")
    public void userServerSavePoint(final long id, final String name) {
    }

    @AfterReturning(value = "execution(* com.feng.learn.service.UserService.save(..))", returning = "ret")
    public void advice2(JoinPoint jp, boolean ret) {
        Trace.add(String.format("0x%016X", Objects.hashCode(this)));
        log.info("advice2 -> ret: {}", ret);
    }

    @AfterReturning(value = "userServerSavePoint(id,name)", returning = "ret", argNames = "jp,name,id,ret")
    public void advice1(JoinPoint jp, final String name, long id, boolean ret) {
        Trace.add(String.format("0x%016X", Objects.hashCode(this)));
        log.info("advice1 -> args: {}", Arrays.toString(jp.getArgs()));
        log.info("advice1 -> ret: {}", ret);
    }

}
