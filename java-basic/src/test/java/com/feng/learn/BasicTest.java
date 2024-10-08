package com.feng.learn;

import static org.assertj.core.api.BDDAssertions.then;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/22
 */
public class BasicTest {


  /**
   * 测试 RoundingMode.UP
   */
  @Test
  void givenBigDecimal_whenDivideBy2_then() {
    BigDecimal _336 = new BigDecimal("3.36");
    BigDecimal _168 = _336.divide(new BigDecimal(2), 2, RoundingMode.UP);
    then(_168.toString()).isEqualTo("1.68");
  }

  /**
   * 测试 RoundingMode.UP 2
   */
  @Test
  void givenBigDecimal_whenDivideBy2_then2() {
    BigDecimal target = new BigDecimal("2.01");
    BigDecimal result = target.divide(new BigDecimal(2), 2, RoundingMode.UP);
    then(result.toString()).isEqualTo("1.01");
  }

  /**
   * 测试 RoundingMode.UP 3
   */
  @Test
  void givenBigDecimal_whenDivideBy2_then3() {
    BigDecimal target = new BigDecimal("2.001");
    BigDecimal result = target.divide(new BigDecimal(2), 2, RoundingMode.UP);
    then(result.toString()).isEqualTo("1.01");
  }


  @Test
  void givenIntegerMinValue_whenMinusOne_then() {
    int i = Integer.MIN_VALUE;
    int ret = i - 1;
    then(ret).isGreaterThan(0);
  }

}
