package com.github.learn.spring.rabbitmq.origin;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/01
 */
@Slf4j
public class RabbitConsumerWithSharedExecutor {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 会被一个 CF 下所有的 Connection 公用
        final ExecutorService executorService =
            Executors.newFixedThreadPool(4, (r) -> new Thread(r, "rabbit-shared-thread"));
        String QUEUE = "queue.test";
        ConnectionFactory cf = new ConnectionFactory();
        cf.setSharedExecutor(executorService);
        Connection connection = cf.newConnection();
        Channel channel = connection.createChannel();
        // 1. declare QUEUE
        // 2. send message
        channel.queueDeclare(QUEUE, false, false, false, null);
        channel.basicConsume(QUEUE, new HelloConsumer(channel));
    }

    public static class HelloConsumer extends DefaultConsumer {

        /**
         * Constructs a new instance and records its association to the passed-in channel.
         *
         * @param channel the channel to which this consumer is attached
         */
        public HelloConsumer(Channel channel) {
            super(channel);
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
            throws IOException {
            log.info("tag={}, envelope={}, properties={}, msg={}",
                consumerTag, envelope, properties, new String(body, StandardCharsets.UTF_8));
            getChannel().basicAck(envelope.getDeliveryTag(), false);
        }

    }

}
