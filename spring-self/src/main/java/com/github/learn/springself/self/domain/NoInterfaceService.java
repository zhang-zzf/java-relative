package com.github.learn.springself.self.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@Service
@Slf4j
public class NoInterfaceService {

    private NoInterfaceService self;

    public void methodA() {
        self.methodB();
    }

    void methodB() {
        log.info("methodB");
    }

    @Autowired
    public void setSelf(NoInterfaceService self) {
        this.self = self;
    }

}
