package com.feng.learn.ut;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Slf4j
public class _01AnnotationTest {

    /**
     * 类初始化时，执行次方法。只会被执行一次
     */
    @BeforeClass
    public static void beforeClass() {
        log.info("@BeforeClass will be executed by junit once and only once.");
    }

    /**
     * 此单元测试执行完成后执行一次此方法。
     * <p>用于清理全局资源</p>
     */
    @AfterClass
    public static void afterClass() {
        log.info("@AfterClass will be executed by junit once and only once.");
    }

    @Before
    public void beforeEachMethod() {
        log.info("@Before will be executed by junit before each test method.");
    }

    @After
    public void afterEachMethod() {
        log.info("@After will be executed by junit after each test method.");
    }

    @Test
    public void test() {
        log.info("Hello, Test");
    }

    /**
     * 测试通过
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExpected() {
        throw new IllegalArgumentException();
    }

    /**
     * 测试失败
     */
    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testNotThrowExpected() {

    }


    /**
     * 测试失败
     */
    @Ignore
    @Test()
    public void testNoExpected() {
        throw new IllegalArgumentException();
    }

    /**
     * 测试失败
     */
    @Ignore
    @Test(timeout = 10L)
    public void testTimeout() throws InterruptedException {
        Thread.sleep(100);
    }

    /**
     * 测试通过
     */
    @Test()
    public void testNoTimeout() throws InterruptedException {
        Thread.sleep(100);
    }

    @Ignore
    @Test
    public void testIgnore() {
        log.info("this test is ignore by junit.");
    }
}
