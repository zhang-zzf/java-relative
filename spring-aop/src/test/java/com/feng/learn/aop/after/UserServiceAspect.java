package com.feng.learn.aop.after;

import static com.feng.learn.util.Trace.objectHex;

import com.feng.learn.util.Trace;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
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
@Slf4j
@Component
public class UserServiceAspect {

    @After(value = "execution(* com.feng.learn.service.UserService.save(..)) && args(id, name)", argNames = "jp,id,name")
    public void advice2(JoinPoint jp, long id, String name) {
        Trace.add(String.format("0x%016X", Objects.hashCode(this)));
        log.info("advice2 -> args: {}", Arrays.toString(jp.getArgs()));
        log.info("advice2 -> this: {}, jp.this: {}, jp.target: {}",
            objectHex(this), objectHex(jp.getThis()), objectHex(jp.getTarget()));
    }


    @After("execution(* com.feng.learn.service.UserService.save(..))")
    public void advice1(JoinPoint jp) {
        Trace.add(String.format("0x%016X", Objects.hashCode(this)));
        log.info("advice1 -> args: {}", Arrays.toString(jp.getArgs()));
        log.info("advice1 -> this: {}, jp.this: {}, jp.target: {}",
            objectHex(this), objectHex(jp.getThis()), objectHex(jp.getTarget()));
    }

}
