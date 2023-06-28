package com.github.learn.leetcode.domain.service.sqrt;

import javax.validation.constraints.PositiveOrZero;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/15
 */
public interface SqrtService {

  /**
   * <p>开根号</p>
   * <p>实现语言不限，你条件可以比上述更加苛刻，但不能宽松。例如调用你的接口 sqrt(9, 0.21) 返回值属于 [2.79, 3.21] 这个区间的任意一个都满足条件。</p>
   *
   * @param v 参数
   * @param t 误差
   * @return 平方根
   */
  @PositiveOrZero double sqrt(@PositiveOrZero int v, @PositiveOrZero double t);

}
