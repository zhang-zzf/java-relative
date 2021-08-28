package com.github.learn.leetcode.domain.service.dp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/28
 */
@SpringBootTest
class MaxSumInArrayServiceTest {

    @Autowired
    MaxSumInArrayService service;

    @Test
    void given_when_then() {
        int maxSum = service.maxSum(new int[]{1, 2, 4, 1, 7, 8, 3});
        then(maxSum).isEqualTo(15);
    }

}