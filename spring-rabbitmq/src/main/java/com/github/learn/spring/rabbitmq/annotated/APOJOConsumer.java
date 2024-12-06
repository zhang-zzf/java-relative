package com.github.learn.spring.rabbitmq.annotated;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/06
 */
@Service
@Slf4j
public class APOJOConsumer {

    @RabbitListener(bindings = {
        @QueueBinding(
            exchange = @Exchange(name = "springboot.test.exchange", type = ExchangeTypes.TOPIC),
            value = @Queue(name = "springboot.test.queue"),
            key = "#")},
        ackMode = "MANUAL",
        id = AConfiguration.ID,
        concurrency = "2-8",
        containerFactory = AConfiguration.CONTAIN_FACTORY
    )
    public void onMessage(String message, Channel channel,
        @Header(value = AmqpHeaders.RECEIVED_EXCHANGE, required = false) String recvExchange,
        @Header(AmqpHeaders.CONSUMER_QUEUE) String recvQueue,
        @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String recvRoutingKey,
        @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag,
        org.springframework.amqp.core.Message coreMessage) throws IOException {
        try {
            log.info("recv msg: {}", message);
            channel.basicAck(deliveryTag, false);
        } catch (IllegalArgumentException e) {
            // 不需要
            channel.basicNack(deliveryTag, false, false);
        } catch (Throwable e) {
            // 需要重新消费消息的异常
            channel.basicNack(deliveryTag, false, true);
        }
    }

    @RabbitListener(bindings = {
        @QueueBinding(
            exchange = @Exchange(name = "springboot.test.exchange2", type = ExchangeTypes.TOPIC),
            value = @Queue(name = "springboot.test.queue2"),
            key = "#")},
        ackMode = "MANUAL",
        id = BConfiguration.ID,
        concurrency = "2-8",
        containerFactory = BConfiguration.CONTAIN_FACTORY
    )
    public void onMessage2(String message, Channel channel,
        @Header(value = AmqpHeaders.RECEIVED_EXCHANGE, required = false) String recvExchange,
        @Header(AmqpHeaders.CONSUMER_QUEUE) String recvQueue,
        @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String recvRoutingKey,
        @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag,
        org.springframework.amqp.core.Message coreMessage) throws IOException {
        try {
            log.info("recv msg: {}", message);
            channel.basicAck(deliveryTag, false);
        } catch (IllegalStateException e) {
            // 需要重新消费消息的异常
            channel.basicNack(deliveryTag, false, true);
        } catch (IllegalArgumentException e) {
            // 不需要
            channel.basicNack(deliveryTag, false, false);
        }
    }

}
