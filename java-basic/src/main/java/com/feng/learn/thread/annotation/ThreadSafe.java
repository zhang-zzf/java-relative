package com.feng.learn.thread.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author zhangzhanfeng
 * @date Dec 10, 2017
 */
@Target({ElementType.TYPE})
public @interface ThreadSafe {

    // String[] authors() default "";
    String[] authors() default "";


}
