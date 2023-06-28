package com.feng.learn.pattern.singleton;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/05
 */
public class PrivateConstructor {


  private PrivateConstructor() {

  }

  /**
   * OK
   */
  public static class SubPrivateConstructor extends PrivateConstructor {

  }

}
