package com.github.learn.leetcode.domain.service.common;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/21
 */
@SpringBootTest
class StringReverseTest {

  @Autowired
  StringReverse stringReverse;

  @Test
  void given_when_then() {
    char[] chars = "hello".toCharArray();
    stringReverse.reverse(chars);
    then(chars).containsExactly('o', 'l', 'l', 'e', 'h');
  }

}