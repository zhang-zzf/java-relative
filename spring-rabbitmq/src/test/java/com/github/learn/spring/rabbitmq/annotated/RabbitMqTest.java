package com.github.learn.spring.rabbitmq.annotated;

import com.github.learn.spring.rabbitmq.SpringRabbitmqApplication;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/06
 */
@SpringBootTest(classes = SpringRabbitmqApplication.class)
public class RabbitMqTest {

    @Autowired
    @Qualifier(AConfiguration.RABBIT_TEMPLATE)
    RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage() {
        rabbitTemplate.convertAndSend("springboot.test.exchange", "aKey", "Hello, World");
    }

}
