package com.github.learn.springself.self.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@Service
public class NoInterfaceService {

    private NoInterfaceService self;

    public void methodA() {
        self.methodB();
    }

    public void methodB() {
    }

    @Autowired
    public void setSelf(NoInterfaceService self) {
        this.self = self;
    }

}
