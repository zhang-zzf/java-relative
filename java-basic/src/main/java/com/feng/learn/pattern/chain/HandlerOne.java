package com.feng.learn.pattern.chain;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/14
 */
public class HandlerOne extends AbstractHandler {

  @Override
  public Object handle(Object in) {
    if (canHandle(in)) {
      // handle in
      return null;
    }
    // next handler handle in
    return handleBySuccessor(in);
  }

  private boolean canHandle(Object in) {
    return false;
  }

}
