package com.feng.learn.assertJ;

import lombok.Value;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/19
 */
public class _00AssertThat {

    final String STR = "Hello, World";

    @Test
    void givenAssertJ_whenLearn_thenDemo() {
        assertThat(STR)
            .isEqualTo(STR).isNotEqualTo("").isEqualToIgnoringCase(STR)
            .startsWith("He").endsWith("ld").contains(", ")
            .isNotNull().isNotBlank().isNotEmpty()
            .hasSizeBetween(1, 20).hasSize(12).hasLineCount(1)
            .isOfAnyClassIn(String.class).isInstanceOfAny(CharSequence.class);

        assertThatThrownBy(() -> { throw new IllegalArgumentException("test"); })
            .isInstanceOf(RuntimeException.class)
            .hasMessage("test");
        // BDD
        // when
        Throwable throwable = catchThrowable(() -> {throw new Exception("exception");});
        // then
        assertThat(throwable).isOfAnyClassIn(Throwable.class, Exception.class)
            .isInstanceOf(Throwable.class)
            .hasMessageContaining("exception");
        //assertThatThrownBy(() -> {}).doesNotThrowAnyException(); // 错误用法
        assertThatCode(() -> {}).doesNotThrowAnyException();

        Person me = Person.valueOf("zhang.zzf", 28);
        assertThat(me).extracting(Person::getName).isOfAnyClassIn(String.class);
    }

    @Test
    void givenAssertJ_whenBDD_thenDemo() {

        // given
        // when

        // then
        then(STR).isEqualTo(STR).isNotEqualTo("").isEqualToIgnoringCase(STR)
            .startsWith("He").endsWith("ld").contains(", ")
            .isNotNull().isNotBlank().isNotEmpty()
            .hasSizeBetween(1, 20).hasSize(12).hasLineCount(1)
            .isOfAnyClassIn(String.class).isInstanceOfAny(CharSequence.class);

        Throwable throwable = catchThrowable(() -> {throw new Exception("exception");});
        // then
        then(throwable).isOfAnyClassIn(Throwable.class, Exception.class)
            .isInstanceOf(Throwable.class)
            .hasMessageContaining("exception");

        // when
        Person me = Person.valueOf("zhang.zzf", 28);
        //then
        then(me).matches(p -> p.getAge() < 30 && p.getName().startsWith("zhang"))
            .extracting(Person::getName).isOfAnyClassIn(String.class);
    }

    @Test
    void testMultiFieldOfObject() {
        Person person = Person.valueOf("zhanfeng.zhang", 22);
        then(person)
            .returns("zhanfeng.zhang", from(Person::getName))
            .returns(22, from(Person::getAge));
    }

}

@Value(staticConstructor = "valueOf")
class Person {

    String name;
    int age;
}
