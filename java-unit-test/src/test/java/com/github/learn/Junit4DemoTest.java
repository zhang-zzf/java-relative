package com.github.learn;

import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author zhanfeng.zhang
 * @date 2022/04/10
 */
public class Junit4DemoTest {

    /**
     * 运行失败的UT
     */
    @Ignore
    @Test
    public void testFail() {
        fail();
    }

    /**
     * 方法用时测试
     */
    @Ignore
    @Test(timeout = 10L)
    public void testTimeout() throws InterruptedException {
        Thread.sleep(100);
    }


}
