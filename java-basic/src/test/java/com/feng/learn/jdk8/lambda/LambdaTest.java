package com.feng.learn.jdk8.lambda;

import java.util.Arrays;
import java.util.Comparator;
import org.junit.jupiter.api.Test;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-08
 */
public class LambdaTest {


    static class ComparatorProvider {
        static int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }
    }

    static class ComparatorProvider2 {
        int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }
    }

    @Test
    void givenFunctionInterface_whenMethodReference_then() {
        String[] stringArray = new String[10];
        // Comparator
        Arrays.sort(stringArray, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        // 语法糖
        Arrays.sort(stringArray, (s1, s2) -> s1.compareToIgnoreCase(s2));
        // 语法糖
        Arrays.sort(stringArray, String::compareToIgnoreCase);
        // 静态方法
        Arrays.sort(stringArray, ComparatorProvider::compare);
        // 实例方法
        Arrays.sort(stringArray, new ComparatorProvider2()::compare);
    }

}
