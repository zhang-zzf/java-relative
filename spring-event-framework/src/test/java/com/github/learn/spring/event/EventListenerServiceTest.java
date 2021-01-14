package com.github.learn.spring.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/14
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    EventBusService.class,
    EventListenerService.class,
})

public class EventListenerServiceTest {

    @Autowired
    EventBusService eventBusService;

    @Test
    public void handleEvent() {
        eventBusService.publish(new Event().setId(1L));
        // 观察日志输出
    }

}