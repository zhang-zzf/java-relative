package com.github.learn.leetcode.domain.service.dp;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/28
 */
@SpringBootTest
class SumEqualNumberInArrayTest {

    @Autowired
    SumEqualNumberInArray service;

    @Test
    void given_when_then() {
        then(service.equal(new int[]{1}, 1)).isTrue();
        then(service.equal(new int[]{1}, 2)).isFalse();
        then(service.equal(new int[]{3, 34, 4, 12, 5, 2}, 9)).isTrue();
    }

}