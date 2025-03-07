package com.github.zzf.learn.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.METHOD,})
public @interface Idempotent {
    @AliasFor("by")
    String[] value() default {};

    @AliasFor("value")
    String[] by() default {};
}
