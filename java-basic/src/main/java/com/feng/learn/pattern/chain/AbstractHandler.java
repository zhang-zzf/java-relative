package com.feng.learn.pattern.chain;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/14
 */
public abstract class AbstractHandler {

  /**
   * 接任 handler
   */
  protected AbstractHandler successor;

  /**
   * 处理器
   *
   * @param in 待处理数据
   * @return 结果
   */
  public abstract Object handle(Object in);

  public void setSuccessor(AbstractHandler successor) {
    this.successor = successor;
  }

  public Object handleBySuccessor(Object in) {
    if (successor != null) {
      return successor.handle(in);
    }
    return null;
  }

}
