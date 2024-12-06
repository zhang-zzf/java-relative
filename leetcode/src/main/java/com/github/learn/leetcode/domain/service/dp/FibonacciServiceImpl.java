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
public class FibonacciServiceImpl implements FibonacciService {

    @Override
    public long fib(@Min(1) @Max(100) int n) {
        if (n == 1 || n == 2) {
            return 1;
        }
        return fib(n - 1) + fib(n - 2);
    }
}
