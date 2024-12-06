package com.feng.learn.pattern.singleton;

import static org.assertj.core.api.BDDAssertions.then;

import com.feng.learn.pattern.singleton.PrivateConstructor.SubPrivateConstructor;
import org.junit.jupiter.api.Test;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/05
 */
class PrivateConstructorTest {

    @Test
    void test() {
        final SubPrivateConstructor instance = new SubPrivateConstructor();
        then(instance).isNotNull();
    }

}