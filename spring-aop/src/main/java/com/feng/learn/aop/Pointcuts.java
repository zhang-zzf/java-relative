package com.feng.learn.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/19
 */
public class Pointcuts {

    /**
     * 容器中所有的set方法
     */
    @Pointcut("execution(* set*(..))")
    public void allSetMethods() {
    }

    /**
     * 容器中所有的公共方法
     */
    @Pointcut("execution(public * *(..))")
    public void allPublicMethods() {
    }

    /**
     * 某个包下所有的类的所有方法
     */
    @Pointcut("within(com.feng.learn.controller.*)") // package
    //@Pointcut("execution(* com.feng.learn.controller.*.*(..))")
    //@Pointcut("within(com.feng.learn.service..*)") // package and sub-package
    //@Pointcut("execution(* com.feng.learn.service..*.*(..))") // package and sub-package
    public void allMethodsInPackage() {
    }


}
