package com.github.learn.springself.aop;

import com.github.learn.springself.aop.api.AopInterfaceService;
import com.github.learn.springself.aop.domain.AopNoInterfaceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@SpringBootTest
class AopSelfTest {

    @Autowired
    AopNoInterfaceService aopNoInterfaceService;
    @Autowired
    AopInterfaceService aopInterfaceService;

    @Test
    void test() {
        aopNoInterfaceService.methodA();
        aopInterfaceService.methodA();
    }

}