package com.feng.learn.assertJ;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

import java.util.function.Function;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/08
 */
@RunWith(MockitoJUnitRunner.class)
public class _01Bdd {

    @Mock
    Function<String, Integer> function;

    @Test
    public void exception() {
        // given: mock stub
        given(function.apply(any())).willThrow(new IllegalArgumentException(""));
        // when
        Throwable throwable = catchThrowable(() -> function.apply(""));
        // then
        assertThat(throwable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("");
        // 对接口返回值断言
        then(throwable)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("");
        // verify: method invoke
        BDDMockito.then(function).should(times(1)).apply(any());
    }

    private int parse(String str) {
        throw new IllegalArgumentException("");
    }

}
