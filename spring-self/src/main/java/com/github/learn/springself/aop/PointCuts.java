package com.github.learn.springself.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@Component
@Aspect
@EnableAspectJAutoProxy()
@Slf4j
public class PointCuts {

    @Before("execution(* com.github.learn.springself.aop..*.methodB(..))")
    public void beforeMethodB() {
        log.info("beforeMethodB.");
    }

}
