package com.github.learn.leetcode.domain.service.dp;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/27
 */
@Service
@Validated
@RequiredArgsConstructor
public class FibonacciServiceDpImpl implements FibonacciService {

    @Override
    public long fib(@Min(1) @Max(100) int n) {
        long[] dp = new long[n < 2 ? 2 : n];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i < n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n - 1];
    }
}
