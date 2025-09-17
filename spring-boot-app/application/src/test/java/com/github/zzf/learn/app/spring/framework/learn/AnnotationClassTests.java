package com.github.zzf.learn.app.spring.framework.learn;

import static org.assertj.core.api.BDDAssertions.then;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.junit.jupiter.api.Test;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-06-23
 */
public class AnnotationClassTests {

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @interface InheritedAnnotationInterface {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface NonInheritedAnnotationInterface {
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @interface InheritedAnnotationClass {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface NonInheritedAnnotationClass {
    }

    @InheritedAnnotationInterface
    @NonInheritedAnnotationInterface
    interface MyInterface {
    }

    @InheritedAnnotationClass
    @NonInheritedAnnotationClass
    class ParentClass {
        public void parentMethod() {
        }
    }

    class ChildClass extends ParentClass implements MyInterface {
        @Override
        public void parentMethod() {
        }
    }


    @Test
    void givenClass_whenGetAnnotations_then() {
        Class<ChildClass> childClazz = ChildClass.class;
        Annotation[] childClazzAnnotations = childClazz.getAnnotations();
        Annotation[] childClazzDeclaredAnnotations = childClazz.getDeclaredAnnotations();
        // 结论: getDeclaredAnnotations() 获取当前类声明的注解, 不包含继承的注解
        then(childClazzDeclaredAnnotations).hasSize(0);
        // 结论: getAnnotations() 包含从父类继承（@Inherited）的注解，不从接口继承注解
        then(childClazzAnnotations).hasSize(1);
    }


    @Test
    void givenClass_whenGetAnnotation_then() {
        Class<ChildClass> childClazz = ChildClass.class;
        then(childClazz.getAnnotation(InheritedAnnotationClass.class))
            .isNotNull()
            .isInstanceOf(InheritedAnnotationClass.class);
        then(childClazz.getAnnotation(NonInheritedAnnotationClass.class)).isNull();
        then(childClazz.getAnnotation(InheritedAnnotationInterface.class)).isNull();
        then(childClazz.getAnnotation(NonInheritedAnnotationInterface.class)).isNull();
    }
}
