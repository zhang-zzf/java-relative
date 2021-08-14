package com.github.learn.springretry.domain.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/14
 */
@SpringBootTest
class ServiceATest {

    @Autowired
    ServiceA serviceA;

    @Test
    void test() {
        serviceA.methodA();
    }

}