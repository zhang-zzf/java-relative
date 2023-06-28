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
class DigitsPlusOneTest {

  @Autowired
  DigitsPlusOne digitsPlusOne;

  @Test
  void givenZero_when_then() {
    int[] digits = {0};
    then(digitsPlusOne.plusOne(digits)).containsExactly(1);
  }

  @Test
  void givenCommon_when_then() {
    int[] digits = {1, 2, 3, 9};
    then(digitsPlusOne.plusOne(digits)).containsExactly(1, 2, 4, 0);
  }

  @Test
  void given99_when_then() {
    int[] digits = {9, 9};
    then(digitsPlusOne.plusOne(digits)).containsExactly(1, 0, 0);
  }


}