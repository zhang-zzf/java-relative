package com.feng.learn.pattern.chain2;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/24
 */
public abstract class AbstractBaseHandler implements Handler {

  private Handler successor;

  @Override
  public Handler setNext(Handler successor) {
    this.successor = successor;
    return successor;
  }

  @Override
  public Object handle(Object request) {
    if (successor != null) {
      return successor.handle(request);
    }
    // default behavior for the chain
    return new Object();
  }
}
