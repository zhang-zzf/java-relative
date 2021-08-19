package com.github.learn.leetcode.domain.service.sqrt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.offset;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/15
 */
@SpringBootTest
public class SqrtServiceTest {

    @Autowired
    SqrtService sqrtService;

    /**
     * 参数不能为负数
     */
    @Test
    void givenNegative_whenSqrt_thenException() {
        final Throwable throwable = catchThrowable(() -> sqrtService.sqrt(-1, 0.1));
        then(throwable).isNotNull().isInstanceOf(ConstraintViolationException.class);
    }

    /**
     * 正常 case
     */
    @Test
    void given_whenSqrt_thenSuccess() {
        then(sqrtService.sqrt(9, 0.1)).isCloseTo(3.0, offset(0.1));
        then(sqrtService.sqrt(10, 0.1)).isCloseTo(3.16, offset(0.1));
        then(sqrtService.sqrt(4, 0.21)).isCloseTo(2, offset(0.21));
        then(sqrtService.sqrt(10000000, 0.000001)).isCloseTo(3162.277660168, offset(0.000001));
    }

    /**
     * 0
     */
    @Test
    void givenZero_whenSqrt_thenSuccess() {
        then(sqrtService.sqrt(0, 0.1)).isCloseTo(0, offset(0.1));
    }

}
