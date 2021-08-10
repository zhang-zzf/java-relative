package com.github.learn.springself.aop.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@Service
public class AopNoInterfaceService {

    AopNoInterfaceService self;

    public void methodA() {
        self.methodB();
    }

    public void methodB() {

    }

    @Autowired
    public void setSelf(AopNoInterfaceService self) {
        this.self = self;
    }

}
