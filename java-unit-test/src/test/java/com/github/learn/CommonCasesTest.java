package com.github.learn;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

/**
 * @author zhanfeng.zhang
 * @date 2022/04/10
 */
public class CommonCasesTest {

  /**
   * stub void method
   */
  @Test
  public void givenVoidMethod_thenStubThrowException_then() {
    final Person mock = mock(Person.class);
    // given
    // given(mock.setAge(any())).willThrow(new IllegalArgumentException());
    doThrow(new IllegalArgumentException()).when(mock).setName(anyString());
    // when
    final Throwable throwable = catchThrowable(() -> mock.setName(anyString()));
    // then
    then(throwable).isInstanceOfAny(IllegalArgumentException.class);
  }


}

@Getter
@Setter
class Person {

  private String name;
  private int age;

  public static Person valueOf(String name, int age) {
    final Person p = new Person();
    p.setName(name);
    p.setAge(age);
    return p;
  }

}
