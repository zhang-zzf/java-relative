package com.github.learn.springself.aop.api.impl;

import com.github.learn.springself.aop.api.AopInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@Service
public class AopInterfaceServiceImpl implements AopInterfaceService {

    AopInterfaceService self;

    @Override
    public void methodA() {
        self.methodB();
    }

    @Override
    public void methodB() {

    }

    @Autowired
    public void setSelf(AopInterfaceService self) {
        this.self = self;
    }
}
