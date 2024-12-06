package com.github.learn.ut;

import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author zhanfeng.zhang
 * @date 2019/10/22
 */
public class _00StarterTest {

    /**
     * 运行成功的UT
     */
    @Test
    public void testVoid() {

    }

    /**
     * 运行失败的UT
     */
    @Ignore
    @Test
    public void testFail() {
        fail();
    }

    @Ignore
    @Test
    public void testException() {
        throw new RuntimeException();
    }

    /**
     * 被忽略的UT
     */
    @Ignore
    @Test(expected = RuntimeException.class, timeout = 10L)
    public void testIgnore() {

    }


}
