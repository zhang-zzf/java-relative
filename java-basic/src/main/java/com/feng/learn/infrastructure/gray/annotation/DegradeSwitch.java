package com.feng.learn.infrastructure.gray.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法级别灰度注解
 * <p>有此注解的方法称为被降级的方法， degradeTo 指定的方法称为降级方法</p>
 * <p>被降级的方法必须不能是 private 修饰</p>
 * <p>降级方法必须和被降级方法拥有相同方法参数</p>
 * <p>降级方法必须和被降级方法在同一个类中</p>
 * <p>降级方法的修饰符没有要求，可以使用 private 修饰</p>
 *
 * @author zhanfeng.zhang
 * @date 2020/06/12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DegradeSwitch {

    /**
     * 降级接口的方法的名字
     * <p>必须和被降级的接口在同一个类中，且具有相同的方法入参</p>
     */
    String degradeTo();

    /**
     * 配置中心降级开关的名字
     * <p>默认方法的引用</p>
     */
    String key() default "";

}
