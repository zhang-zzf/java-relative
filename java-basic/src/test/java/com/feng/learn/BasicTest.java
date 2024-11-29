package com.feng.learn;

import static org.assertj.core.api.BDDAssertions.then;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import org.junit.jupiter.api.Test;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/22
 */
public class BasicTest {

  /**
   * 测试 BigDecimal 值大小比较
   */
  boolean compareBigDecimal(BigDecimal v1, BigDecimal v2) {
    return (v1 == v2) || (v1 != null && v2 != null && v1.compareTo(v2) == 0);
  }

  /**
   * 测试 BigDecimal 相等
   */
  boolean equalsBigDecimal(BigDecimal v1, BigDecimal v2) {
    return Objects.equals(v1, v2);
  }


  @Test
  void givenBigDecimal_whenCompare_then() {
    then(null == null).isTrue();
    then(compareBigDecimal(null, null)).isTrue();
    then(compareBigDecimal(BigDecimal.ZERO, null)).isFalse();
    then(compareBigDecimal(null, BigDecimal.ZERO)).isFalse();
    then(compareBigDecimal(new BigDecimal(0.0), BigDecimal.ZERO)).isTrue();
    then(compareBigDecimal(new BigDecimal("0.0"), BigDecimal.ZERO)).isTrue();
    BigDecimal v1 = new BigDecimal("0.1");
    then(compareBigDecimal(v1, BigDecimal.ZERO)).isFalse();
    then(compareBigDecimal(v1, new BigDecimal("0.100"))).isTrue();
    // watch out for this case
    then(compareBigDecimal(v1, new BigDecimal(0.1))).isFalse();
    //
    //
    then(equalsBigDecimal(v1, new BigDecimal("0.1"))).isTrue();
    then(equalsBigDecimal(v1, new BigDecimal("0.10"))).isFalse();
  }


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
