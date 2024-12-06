package com.github.learn.spring.rabbitmq.origin;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2020/12/31
 */
@Slf4j
public class RabbitProducer {

    public static void main(String[] args) throws IOException, TimeoutException {
        String QUEUE = "queue.test";
        ConnectionFactory cf = new ConnectionFactory();
        try (Connection connection = cf.newConnection();
            Channel channel = connection.createChannel()) {
            // 1. declare QUEUE
            // 2. send message
            channel.queueDeclare(QUEUE, false, false, false, null);
            channel.basicPublish("", QUEUE, null, "Hello, World!".getBytes(StandardCharsets.UTF_8));
            log.info("msg sent");
        }
    }

}
