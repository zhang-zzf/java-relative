package com.feng.learn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/22
 */
public class BasicTest {

    @Test
    void givenIntegerMinValue_whenMinusOne_then() {
        int i = Integer.MIN_VALUE;
        int ret = i - 1;
        then(ret).isGreaterThan(0);
    }

}
