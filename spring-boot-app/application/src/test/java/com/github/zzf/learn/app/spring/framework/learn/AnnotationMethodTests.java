package com.github.zzf.learn.app.spring.framework.learn;

import static org.assertj.core.api.BDDAssertions.then;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-06-23
 */
public class AnnotationMethodTests {

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

    interface MyInterface {
        @InheritedAnnotationInterface
        @NonInheritedAnnotationInterface
        void methodA();

        @InheritedAnnotationInterface
        @NonInheritedAnnotationInterface
        void methodB();

        @InheritedAnnotationInterface
        @NonInheritedAnnotationInterface
        default void methodBB() {
        }
    }

    abstract static class ParentClass {
        @InheritedAnnotationClass
        @NonInheritedAnnotationClass
        public abstract void methodA();

        @InheritedAnnotationClass
        @NonInheritedAnnotationClass
        public abstract void methodC();

        @InheritedAnnotationClass
        @NonInheritedAnnotationClass
        public void methodCC() {
        }

    }

    static class ChildClass extends ParentClass implements MyInterface {
        @Override
        public void methodA() {

        }

        @Override
        public void methodC() {

        }

        @Override
        public void methodB() {

        }
    }


    @SneakyThrows
    @Test
    void givenInheritedMethod_whenGetAnnotations_then() {
        Class<ChildClass> clazz = ChildClass.class;
        Method methodBB = clazz.getMethod("methodBB");
        // 结论: methodBB 实际上是 MyInterface.methodBB， 可以获取到方法上的注解
        // methodBB
        then(methodBB.getAnnotations()).hasSize(2);
        then(methodBB.getDeclaredAnnotations()).hasSize(2);
        then(methodBB.getAnnotation(InheritedAnnotationInterface.class)).isNotNull();
        then(methodBB.getAnnotation(NonInheritedAnnotationInterface.class)).isNotNull();
        Method methodCC = clazz.getMethod("methodCC");
        // 结论: methodCC 实际上是 ParentClass.methodCC， 可以获取到方法上的注解
        then(methodCC.getAnnotations()).hasSize(2);
        then(methodCC.getDeclaredAnnotations()).hasSize(2);
        then(methodCC.getAnnotation(NonInheritedAnnotationClass.class)).isNotNull();
        then(methodCC.getAnnotation(InheritedAnnotationClass.class)).isNotNull();
    }

    @SneakyThrows
    @Test
    void givenOverrideMethod_whenGetAnnotations_then() {
        Class<ChildClass> clazz = ChildClass.class;
        Method methodA = clazz.getMethod("methodA");
        // 结论: methodA 无法从 ParentClass / MyInterface 继承的注解
        // methodA
        then(methodA.getAnnotations()).hasSize(0);
        then(methodA.getDeclaredAnnotations()).hasSize(0);
        then(methodA.getAnnotation(InheritedAnnotationInterface.class)).isNull();
        then(methodA.getAnnotation(NonInheritedAnnotationInterface.class)).isNull();
        then(methodA.getAnnotation(NonInheritedAnnotationClass.class)).isNull();
        then(methodA.getAnnotation(InheritedAnnotationClass.class)).isNull();
        Method methodB = clazz.getMethod("methodB");
        // 结论: methodB 无法从 ParentClass / MyInterface 继承的注解
        // methodB
        then(methodB.getAnnotations()).hasSize(0);
        then(methodB.getDeclaredAnnotations()).hasSize(0);
        then(methodB.getAnnotation(InheritedAnnotationInterface.class)).isNull();
        then(methodB.getAnnotation(NonInheritedAnnotationInterface.class)).isNull();
        then(methodB.getAnnotation(NonInheritedAnnotationClass.class)).isNull();
        then(methodB.getAnnotation(InheritedAnnotationClass.class)).isNull();
        Method methodC = clazz.getMethod("methodC");
        // 结论: methodC 无法从 ParentClass / MyInterface 继承的注解
        // methodC
        then(methodC.getAnnotations()).hasSize(0);
        then(methodC.getDeclaredAnnotations()).hasSize(0);
        then(methodC.getAnnotation(InheritedAnnotationInterface.class)).isNull();
        then(methodC.getAnnotation(NonInheritedAnnotationInterface.class)).isNull();
        then(methodC.getAnnotation(NonInheritedAnnotationClass.class)).isNull();
        then(methodC.getAnnotation(InheritedAnnotationClass.class)).isNull();
    }




}
