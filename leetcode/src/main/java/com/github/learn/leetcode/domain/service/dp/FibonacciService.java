package com.github.learn.leetcode.domain.service.dp;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/27
 */
public interface FibonacciService {

    /**
     * fibonacci
     *
     * @param n n
     * @return fib(n)
     */
    long fib(@Min(1) @Max(100) int n);

}
