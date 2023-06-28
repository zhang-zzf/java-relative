package com.feng.learn.slf4j;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhanfeng.zhang
 * @date 2021/03/09
 */
@Slf4j
public class Slf4jTest {

  @Test
  public void test() {
    try {
      throw new IllegalArgumentException();
    } catch (Exception e) {
      log.error("Exception: ", e);
    }
  }

}
