package com.github.learn.spring.rabbitmq;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.ImmutableMap;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static java.util.stream.Collectors.toList;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/01
 */
@Slf4j
public class RabbitMqTest {
    final String QUEUE = "queue.test";
    final String shutdown = "shutdown";
    final String durableQueue = "durable.queue.test";

    /**
     * 死信队列
     */
    @Test
    public void givenDLX_whenNack_then() throws IOException, TimeoutException, InterruptedException {
        final String QUEUE = "normal.queue";
        final String DLX_EX = "dlx.exchange.of.normal.queue";
        final String DLX_EX_Q = "queue.of.dlx.exchange";

        ConnectionFactory cf = new ConnectionFactory();
        cf.setThreadFactory(r -> new Thread(r, "rabbit_threads"));
        Connection connection = cf.newConnection();
        // consumerOne
        Channel channel1 = connection.createChannel();
        // declare dead_letter_exchange
        channel1.exchangeDeclare(DLX_EX, BuiltinExchangeType.TOPIC, true);
        /**
         * producer does not know any thing about the queues of his exchange
         // declare a tmp queue bind to the dead_letter_exchange
         channel1.queueDeclare(DLX_EX_Q, true, false, false, null);
         // receive any message that send to the exchange
         channel1.queueBind(DLX_EX_Q, DLX_EX, "#");
         */
        // declare a normal queue with a dlx
        channel1.queueDeclare(QUEUE, true, false, false,
            ImmutableMap.of("x-dead-letter-exchange", DLX_EX));
        // prefetchCount = 1
        channel1.basicQos(1);
        channel1.basicConsume(QUEUE, (consumerTag, message) -> {
            log.info("{} =>({}) {}", consumerTag, QUEUE, new String(message.getBody(), StandardCharsets.UTF_8));
            // requeue must be false
            channel1.basicNack(message.getEnvelope().getDeliveryTag(), false, false);
        }, consumerTag -> {});
        // producer
        Channel channel2 = connection.createChannel();
        for (int i = 0; i < 10; i++) {
            channel2.basicPublish("", QUEUE, null, ("" + i).getBytes(StandardCharsets.UTF_8));
        }
        // dead_letter_exchange_queue_consumer
        Channel channel3 = connection.createChannel();
        // declare dead_letter_exchange
        channel3.exchangeDeclare(DLX_EX, BuiltinExchangeType.TOPIC, true);
        // declare a tmp queue bind to the dead_letter_exchange
        channel3.queueDeclare(DLX_EX_Q, true, false, false, null);
        // receive any message that send to the exchange
        channel3.queueBind(DLX_EX_Q, DLX_EX, "#");
        // consume message from dead_letter_exchange_queue
        channel3.basicConsume(DLX_EX_Q, (consumerTag, message) -> {
            log.info("{} =>({}) {}", consumerTag, DLX_EX_Q, new String(message.getBody(), StandardCharsets.UTF_8));
            channel3.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, consumerTag -> {});
        Thread.currentThread().join();
    }

    /**
     * 一个 Channel 可以注册 N 个 Consumer，一个 Channel 的所有消息同一时间只会在一个线程中被执行（Channel 不存在并发）
     * <p> Connection 可以创建 N 个 Channel</p>
     */
    @Test
    public void givenThreeConsumerAndQos1_when_then() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory cf = new ConnectionFactory();
        cf.setThreadFactory(r -> new Thread(r, "rabbit_threads"));
        Connection connection = cf.newConnection();
        // consumerOne
        Channel channel1 = connection.createChannel();
        // 1. declare QUEUE
        // 2. send message
        channel1.queueDeclare(QUEUE, false, false, false, null);
        // prefetchCount = 1
        channel1.basicQos(1);
        channel1.basicConsume(QUEUE, (consumerTag, message) -> {
            log.info("{} => {}", consumerTag, new String(message.getBody(), StandardCharsets.UTF_8));
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                // ignore
            }
            channel1.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, consumerTag -> {});
        // consumerTwo
        Channel channel2 = connection.createChannel();
        // 1. declare QUEUE
        // 2. send message
        channel2.queueDeclare(QUEUE, false, false, false, null);
        // prefetchCount = 1
        channel2.basicQos(1);
        channel2.basicConsume(QUEUE, (consumerTag, message) -> {
            log.info("{} => {}", consumerTag, new String(message.getBody(), StandardCharsets.UTF_8));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // ignore
            }
            channel2.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, consumerTag -> {});
        // consumer Three
        channel2.basicConsume(QUEUE, (consumerTag, message) -> {
            log.info("{} => {}", consumerTag, new String(message.getBody(), StandardCharsets.UTF_8));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ignore
            }
            channel2.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, consumerTag -> {});
        // send message
        Channel producer1 = connection.createChannel();
        for (int i = 0; i < 1000; i++) {
            producer1.basicPublish("", QUEUE, null, ("" + i).getBytes(StandardCharsets.UTF_8));
        }
        new CountDownLatch(1).await();
    }

    /**
     * 在 exchange 没有绑定任何队列的前提发送消息到 exchange，然后绑定 queue 消费消息。绑定之前的消息会丢失
     * <p>durable 消息也会丢失</p>
     */
    @Test
    public void givenExchangeBindNoQueue_whenSendMsg_thenMsgIsLost()
        throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory cf = new ConnectionFactory();
        cf.setThreadFactory(r -> new Thread(r, "rabbit_threads"));
        Connection connection = cf.newConnection();
        Channel producer1 = connection.createChannel();
        // 声明一个 durable exchange。服务器重启后 exchange 依旧存在
        String exchange = "exchange.test";
        String routingKey = "some.key.to.test";
        producer1.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC, true, true, null);
        for (int i = 0; i < 1000; i++) {
            producer1.basicPublish(exchange, routingKey,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                ("" + i).getBytes());
        }
        Channel consumer1 = connection.createChannel();
        String queue = "some.queue.to.test";
        consumer1.queueDeclare(queue, true, false, true, null);
        consumer1.queueBind(queue, exchange, routingKey);
        // 此时消费不到任何消息
        consumer1.basicConsume(QUEUE, (consumerTag, message) -> {
            log.info("{} => {}", consumerTag, new String(message.getBody(), StandardCharsets.UTF_8));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // ignore
            }
            consumer1.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, consumerTag -> {});
        new CountDownLatch(1).await();
    }

    /**
     * 按消费能力消费
     * <p>channel.basicQos(1);</p>
     * <p>queue 的所有消费者都必须使用上面的配置。若其中一个消费者 A 没有上面的配置，队列中未被消费都消息都会被 A 拿走，尽管 A 没有能力消费</p>
     *
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    @Test
    public void givenTwoConsumerAndQos1_when_then() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory cf = new ConnectionFactory();
        cf.setThreadFactory(r -> new Thread(r, "rabbit_threads"));
        Connection connection = cf.newConnection();
        // consumerOne
        Channel consumer1 = connection.createChannel();
        // 1. declare QUEUE
        // 2. send message
        consumer1.queueDeclare(QUEUE, false, false, false, null);
        // prefetchCount = 1
        consumer1.basicQos(1);
        consumer1.basicConsume(QUEUE, (consumerTag, message) -> {
            log.info("{} => {}", consumerTag, new String(message.getBody(), StandardCharsets.UTF_8));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ignore
            }
            consumer1.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, consumerTag -> {});
        // consumerTwo
        Channel consumer2 = connection.createChannel();
        // 1. declare QUEUE
        // 2. send message
        consumer2.queueDeclare(QUEUE, false, false, false, null);
        // prefetchCount = 1
        consumer2.basicQos(1);
        consumer2.basicConsume(QUEUE, (consumerTag, message) -> {
            log.info("{} => {}", consumerTag, new String(message.getBody(), StandardCharsets.UTF_8));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // ignore
            }
            consumer2.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, consumerTag -> {});
        // send message
        Channel producer1 = connection.createChannel();
        for (int i = 0; i < 1000; i++) {
            producer1.basicPublish("", QUEUE, null, ("" + i).getBytes(StandardCharsets.UTF_8));
        }
        new CountDownLatch(1).await();
    }

    /**
     * 默认情况下：每个消费者平均消费整个队列
     * <p>若消费者A消费其中一条消息失败，应该被消费者A消费的其他消息会被阻塞。此时新增加的消费者不会消费已分配给消费者A的消息</p>
     *
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    @Test
    public void givenTwoConsumer_when_then() throws IOException, TimeoutException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        startOneConsumer(latch, 1);
        startOneConsumer(latch, 5000);
        List<String> data = Stream.iterate(0, i -> i + 1).limit(100).map(i -> String.valueOf(i)).collect(toList());
        for (String datum : data) {
            sendMessage(datum);
        }
        latch.await();
    }

    @Test
    public void onlyStartOneConsumer() throws IOException, TimeoutException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        startOneConsumer(latch, 1000);
        latch.await();
    }

    @Test
    public void testSendAndRecv() throws IOException, TimeoutException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        startOneConsumer(latch, 1);
        sendMessage("Hello, World");
        //  sendMessage(shutdown);
        latch.await();
    }

    /**
     * 声明 durableQueue / durableMessage
     *
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    @Test
    public void testDurableQueueAndMessageSendAndRecv() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory cf = new ConnectionFactory();
        cf.setThreadFactory(r -> new Thread(r, "rabbit_threads"));
        Connection connection = cf.newConnection();
        Channel channel = connection.createChannel();
        // 1. declare QUEUE
        // 2. send message
        // 声明 durable queue
        channel.queueDeclare(durableQueue, true, false, false, null);
        channel.basicPublish("", durableQueue,
            // 声明 durable message
            MessageProperties.PERSISTENT_TEXT_PLAIN,
            "DurableMessage".getBytes(StandardCharsets.UTF_8));
        CountDownLatch latch = new CountDownLatch(1);
        channel.basicConsume(durableQueue, ((consumerTag, message) -> {
            log.info("{} => {}", consumerTag, new String(message.getBody(), StandardCharsets.UTF_8));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            latch.countDown();
        }), consumerTag -> {});
        latch.await();
    }

    @Test
    public void onlySendMessage() throws IOException, TimeoutException {
        List<String> data = Stream.iterate(1000, i -> i + 1).limit(20).map(String::valueOf).collect(toList());
        for (String datum : data) {
            sendMessage(datum);
        }
    }

    void sendMessage(String msg) throws IOException, TimeoutException {
        ConnectionFactory cf = new ConnectionFactory();
        try (Connection connection = cf.newConnection();
             Channel channel = connection.createChannel()) {
            // 1. declare QUEUE
            // 2. send message
            channel.queueDeclare(QUEUE, false, false, false, null);
            channel.basicPublish("", QUEUE, null, msg.getBytes(StandardCharsets.UTF_8));
        }
    }

    void startOneConsumer(CountDownLatch latch, int timeToConsumeMessage) throws IOException, TimeoutException {
        ConnectionFactory cf = new ConnectionFactory();
        cf.setThreadFactory(r -> new Thread(r, "rabbit_threads"));
        Connection connection = cf.newConnection();
        Channel channel = connection.createChannel();
        // 1. declare QUEUE
        // 2. send message
        channel.queueDeclare(QUEUE, false, false, false, null);
        channel.basicConsume(QUEUE, (consumerTag, message) -> {
            // log.info("{} recv msg: {}", consumerTag, deliveryMessageToString(message));
            if (shutdown.equals(new String(message.getBody(), StandardCharsets.UTF_8))) {
                latch.countDown();
            }
            log.info("{} => {}", consumerTag, new String(message.getBody(), StandardCharsets.UTF_8));
            try {
                Thread.sleep(timeToConsumeMessage);
            } catch (InterruptedException e) {
                // ignore
            }
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, consumerTag -> {});
    }

    public String deliveryMessageToString(Delivery msg) {
        JSONObject json = new JSONObject();
        json.put("envelope", msg.getEnvelope());
        json.put("properties", msg.getProperties());
        json.put("body", new String(msg.getBody(), StandardCharsets.UTF_8));
        return json.toJSONString();
    }

}