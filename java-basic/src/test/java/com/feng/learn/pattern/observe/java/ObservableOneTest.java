package com.feng.learn.pattern.observe.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/15
 */
class ObservableOneTest {

  // 1个主题，2个观察者
  ObservableOne observableOne;
  ObserverOne observerOne;
  ObserverTwo observerTwo;

  @BeforeEach
  public void beforeEachMethod() {
    observableOne = new ObservableOne();
    observerOne = new ObserverOne(observableOne);
    observerTwo = new ObserverTwo(observableOne);

  }

  @Test
  void setValue() {
    observableOne.setValue(Integer.MAX_VALUE);
  }
}