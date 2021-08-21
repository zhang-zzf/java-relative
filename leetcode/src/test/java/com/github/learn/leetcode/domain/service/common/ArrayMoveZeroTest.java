package com.github.learn.leetcode.domain.service.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/21
 */
@SpringBootTest
class ArrayMoveZeroTest {

    @Autowired
    ArrayMoveZero arrayMoveZero;

    @Test
    void given_when_then() {
        int[] array = {0, 1, 0, 3, 12};
        arrayMoveZero.move(array);
        then(array).containsExactly(1, 3, 12, 0, 0);
    }


}