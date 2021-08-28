package com.github.learn.leetcode.domain.service.dp;

import javax.validation.constraints.NotNull;

/**
 * 数组中 n 个数的和等于 sum
 * <p>数组中都是正整数</p>
 *
 * @author zhanfeng.zhang
 * @date 2021/08/28
 */
public interface SumEqualNumberInArray {

    boolean equal(@NotNull int[] array, int sum);

}
