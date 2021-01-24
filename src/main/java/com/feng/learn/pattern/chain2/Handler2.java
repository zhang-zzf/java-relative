package com.feng.learn.pattern.chain2;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/24
 */
@Slf4j
public class Handler2 extends AbstractBaseHandler {

    @Override
    public Object handle(Object request) {
        if (canHandle(request)) {
            log.info("handle: {}", request);
        }
        return super.handle(request);
    }

    private boolean canHandle(Object request) {
        return false;
    }
}
