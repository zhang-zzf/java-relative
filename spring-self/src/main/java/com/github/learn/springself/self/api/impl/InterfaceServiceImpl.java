package com.github.learn.springself.self.api.impl;

import com.github.learn.springself.self.api.InterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@Service
public class InterfaceServiceImpl implements InterfaceService {

    /**
     * 注入接口自己
     */
    private InterfaceService self;

    @Override
    public void methodA() {
        self.methodB();
    }

    @Override
    public void methodB() {
    }

    @Autowired
    public void setSelf(InterfaceService self) {
        this.self = self;
    }

}
