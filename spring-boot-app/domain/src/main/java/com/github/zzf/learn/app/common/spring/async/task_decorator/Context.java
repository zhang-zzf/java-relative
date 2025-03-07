package com.github.zzf.learn.app.common.spring.async.task_decorator;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
public class Context {

    public static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

}