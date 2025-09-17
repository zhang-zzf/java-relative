package com.github.zzf.learn.app.spring.framework.learn;

import static org.assertj.core.api.BDDAssertions.then;

import com.github.zzf.learn.app.Application;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-06-23
 */
public class SpringTests {


    interface A {

    }

    interface B {

    }

    interface AB extends A, B {

    }


    @Test
    void givenInterface_when_then() {
        Class<AB> abClass = AB.class;
        // 结论: 接口继承接口，是当前接口的父接口，而不是父类
        then(abClass.getInterfaces()).hasSize(2);
        then(abClass.getSuperclass()).isNull();
    }

    @Test
    void givenInheritedAnnotation_whenGetAnnotation_then() {
        Component mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(Application.class, Component.class);
        then(mergedAnnotation).isNotNull();
        then(mergedAnnotation.value()).isEqualTo("");
    }

}