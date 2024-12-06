package com.feng.learn.pattern.chain;

import org.junit.Before;
import org.junit.Test;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/14
 */
public class HandlerManagerTest {

    private AbstractHandler abstractHandler;

    @Before
    public void init() {
        HandlerManager h = new HandlerManager();
        h.addLast(new HandlerOne());
        h.addLast(new HandlerTwo());
        abstractHandler = h;
    }

    @Test
    public void handle() {
        abstractHandler.handle(new Object());
    }

}