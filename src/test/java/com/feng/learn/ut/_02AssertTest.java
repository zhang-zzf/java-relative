package com.feng.learn.ut;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.feng.learn.util.MathUtil;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author zhanfeng.zhang
 * @date 2019/10/22
 */
public class _02AssertTest {

    public static final String STR = "Hello, World";

    @Test
    public void testNoAssert() {
        int sum = MathUtil.add(1, 2);
        System.out.println(sum);
    }

    @Ignore
    @Test
    public void testAssert() {
        assertEquals(3, MathUtil.add(1, 2));

        // assertEquals(4, MathUtil.add(1, 2));
        assertEquals("测试add 方法不符合需求", 4, MathUtil.add(1, 2));

        // 按需求参考  org.junit.Assert.* 静态方法
    }

    @Test
    public void testAssertThat() {
        assertThat(1, is(1));
        assertThat(Arrays.asList(1, 2, 3, 4), hasItem(1));
    }

    /**
     * 判断型
     */
    @Test
    public void testAssertThatDecideType() {
        // always match
        assertThat(new Object(), anything());

        // any(Class<?> clazz)
        Matcher<Integer> anyInteger = any(Integer.class);
        assertThat(2, anyInteger);

        // isA
        assertThat(2, isA(Integer.class));

        // 字符串
        assertThat(STR, containsString("llo"));
        assertThat(STR, startsWith("Hell"));
        assertThat(STR, endsWith("ld"));

        // not(T)
        Matcher<Integer> notFive = not(5);
        Matcher<String> notHelloWorld = not(STR);

        // is(T)
        Matcher<Integer> isFive = is(5);
        // is(Matcher<T>) 下面2个作用上是相同的
        Matcher<String> isContainsStringHe = is(containsString("He"));
        Matcher<String> isContainsStringHe2 = containsString("He");

        // instanceof
        assertThat(STR, is(instanceOf(String.class)));
        // ==
        assertThat(STR, is(sameInstance(STR)));
        // equals
        assertThat(STR, is(equalTo(STR)));

        assertThat(STR, is(not(nullValue())));
        assertThat(STR, is(notNullValue()));

    }

    /**
     * 组合型
     */
    @Test
    public void testAssertThatCombineType() {
        // &&
        assertThat(STR, allOf(startsWith("H"), endsWith("d")));
        // ||
        assertThat("Hello, World", anyOf(containsString("llo"), endsWith("dd")));
        // !
        assertThat(STR, not(containsString("me")));

        // 可用allOf / anyOf 替代
        assertThat(STR, both(containsString("He")).and(endsWith("ld")));
        assertThat(STR, either(containsString("He")).or(containsString("me")));
    }

    @Test
    public void testAssertThatIteratorType() {
        List<String> list = Arrays.asList("Hello", "World");

        assertThat(list, hasItem(startsWith("Hel")));
        assertThat(list, hasItems(endsWith("ld"), containsString("el")));

        assertThat(list, hasItem("Hello"));
        assertThat(list, hasItems("Hello", "World"));

        assertThat(list, everyItem(containsString("l")));
        assertThat(list, everyItem(notNullValue(String.class)));
        assertThat(list, everyItem(instanceOf(String.class)));
    }

    @Test
    public void testAssertThatPrimitiveTypes() {
        // 5(int) equals 5;
        assertThat(5, is(5));
        assertThat(Integer.valueOf(5), is(5));
        assertThat((byte) 5, is(not(5)));
    }

}
