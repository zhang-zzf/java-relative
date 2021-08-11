package com.github.learn.spring_cycle_dependency.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/11
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceA {

    private ServiceB serviceB;

    @Autowired
    private void setServiceB(ServiceB serviceB) {
        this.serviceB = serviceB;
    }

    public void methodA() {
        log.info("serviceA.methodA");
    }


    public void methodB() {
        log.info("serviceA.methodB");
        serviceB.methodB();
    }
}
