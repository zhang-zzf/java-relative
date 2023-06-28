package com.github.learn.leetcode.domain.service.dp;

import javax.validation.constraints.NotNull;

/**
 * 不相邻数的和的最大值
 *
 * @author zhanfeng.zhang
 * @date 2021/08/28
 */
public interface MaxSumInArrayService {

  int maxSum(@NotNull int[] array);

}
