package com.feng.learn.spring.proxy;

import com.feng.learn.spring.proxy.class_based.NoInterfaceService;
import com.feng.learn.spring.proxy.interface_based.AInterface;
import com.feng.learn.spring.proxy.interface_based.AInterfaceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author zhanfeng.zhang
 * @date 2020/5/30
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    ProxyTargetClassTrueTest.ProxyTargetClassContext.class,
    AInterfaceService.class,
    NoInterfaceService.class
})
public class ProxyTargetClassTrueTest {

    @Autowired
    AInterface interfaceService;

    @Autowired
    NoInterfaceService noInterfaceService;

    @Test
    public void givenProxyTargetClassTrue_then() {
        then(interfaceService)
            .isInstanceOf(AInterface.class)
            .isInstanceOf(AInterfaceService.class)
            .isNotInstanceOf(Proxy.class);
        then(noInterfaceService)
            .isInstanceOf(NoInterfaceService.class);
    }

    @Configuration
    @EnableAsync(mode = AdviceMode.PROXY, proxyTargetClass = true)
    public static class ProxyTargetClassContext {
    }
}