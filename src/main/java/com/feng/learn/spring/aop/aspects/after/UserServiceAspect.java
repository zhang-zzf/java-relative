package com.feng.learn.spring.aop.aspects.after;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.SourceLocation;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Objects;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/19
 */
@Aspect
@EnableAspectJAutoProxy
@Configuration
@Slf4j
public class UserServiceAspect {

    @After(value = "execution(* com..UserService.save(..)) && args(id, name)", argNames = "jp,id,name")
    public void afterSaveMethodOfUserService1(JoinPoint jp, long id, String name) {
        Object[] args = jp.getArgs();
        String kind = jp.getKind();
        Signature signature = jp.getSignature();
        Class declaringType = signature.getDeclaringType();
        String declaringTypeName = signature.getDeclaringTypeName();
        String name1 = signature.getName();
        SourceLocation sourceLocation = jp.getSourceLocation();
        JoinPoint.StaticPart staticPart = jp.getStaticPart();
        Object target = jp.getTarget();
        Object aThis = jp.getThis();
        String longString = jp.toLongString();
        String shortString = jp.toShortString();
        String toString = jp.toString();
        log.info("this=>{}, jp.getThis=>{}, target=>{}", Objects.hashCode(this), Objects.hashCode(aThis),
            Objects.hashCode(target));

    }

    @After("execution(* com..UserService.save(..))")
    public void afterSaveMethodOfUserService2(JoinPoint jp) {
        log.info("this=>{}, jp.getThis=>{}, target=>{}", Objects.hashCode(this), Objects.hashCode(jp.getThis()),
            Objects.hashCode(jp.getTarget()));
        throw new IllegalArgumentException("afterSaveMethodOfUserService");
    }

}
