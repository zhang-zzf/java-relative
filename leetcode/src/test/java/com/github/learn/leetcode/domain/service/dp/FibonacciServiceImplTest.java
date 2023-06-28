package com.github.learn.leetcode.domain.service.dp;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/27
 */
@SpringBootTest
class FibonacciServiceImplTest {

  @Autowired
  @Qualifier("fibonacciServiceDpImpl")
  FibonacciService fibonacciService;

  @Test
  void given_when_then() {
    then(fibonacciService.fib(1)).isEqualTo(1);
    then(fibonacciService.fib(2)).isEqualTo(1);
    then(fibonacciService.fib(3)).isEqualTo(2);
    then(fibonacciService.fib(4)).isEqualTo(3);
    then(fibonacciService.fib(8)).isEqualTo(21);
  }

}