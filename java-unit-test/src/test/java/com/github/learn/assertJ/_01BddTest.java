package com.github.learn.assertJ;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

import com.github.learn.AbstractJUnit4Mockito;
import java.util.function.Function;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/08
 */
public class _01BddTest extends AbstractJUnit4Mockito {

  @Mock
  Function<String, Integer> function;

  @Test
  public void exception() {
    // given: mock stub
    given(function.apply(any())).willThrow(new IllegalArgumentException(""));
    // when
    Throwable throwable = catchThrowable(() -> function.apply(""));
    // then
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
