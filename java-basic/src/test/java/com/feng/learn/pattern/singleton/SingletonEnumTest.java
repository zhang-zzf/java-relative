package com.feng.learn.pattern.singleton;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class SingletonEnumTest {

  @Test
  void givenEnum_whenSingleton_then() {
    SingletonEnum instance = SingletonEnum.INSTANCE;
    then(instance.getStr()).isNull();
    instance.setStr("Hello");
    then(instance.getStr()).isEqualTo("Hello");
  }

}