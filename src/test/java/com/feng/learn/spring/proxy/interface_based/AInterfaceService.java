package com.feng.learn.spring.proxy.interface_based;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2020/5/30
 */
@Service
public class AInterfaceService implements AInterface {

    @Async
    @Override
    public void methodA() {

    }
}
