package com.feng.learn.pattern.chain2;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/24
 */
public interface Handler {

  /**
   * next handler
   *
   * @param successor next processor in the chain
   * @return successor
   */
  Handler setNext(Handler successor);

  /**
   * handle a command
   *
   * @param request command
   * @return response
   */
  Object handle(Object request);

}
