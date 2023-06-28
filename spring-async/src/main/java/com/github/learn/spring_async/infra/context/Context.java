package com.github.learn.spring_async.infra.context;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
public class Context {

  public static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

}