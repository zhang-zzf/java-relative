package com.github.learn.spring.rabbitmq.java_based;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/03
 */
@Configuration
@Slf4j
public class AsyncRabbitConsumerConfiguration {

    public static final String ID = "asyncConsumer";
    public static final String AMQP_ADMIN = ID + "AmqpAdmin";
    public static final String RABBIT_TEMPLATE = ID + "RabbitTemplate";
    /**
     * 若无发送消息的需求，可以不配置此 Bean
     *
     * @param cf
     * @return
     * @Bean(RABBIT_TEMPLATE) public RabbitTemplate rabbitTemplate(@Qualifier(C_F) ConnectionFactory cf) {
     * RabbitTemplate template = new RabbitTemplate(cf); return template; }
     */
    public static final String SPRING_TEST_EXCHANGE = ID + "SpringTestExchange";
    public static final String SPRING_TEST_EXCHANGE_DLX = ID + "SpringTestExchangeDlx";
    public static final String SPRING_TEST_QUEUE = ID + "SpringTestQueue";
    private static final String C_F = ID + "connectionFactory";

    @Bean(ID + "SimpleMessageListenerContainer")
    public SimpleMessageListenerContainer messageListenerContainer(
        @Qualifier(C_F) ConnectionFactory cf,
        @Qualifier(SPRING_TEST_QUEUE) Queue springTestQueue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(cf);
        container.addQueues(springTestQueue);
        container.setMessageListener(exampleListener());
        return container;
    }

    @Bean(C_F)
    public ConnectionFactory connectionFactory() {
        // todo 读取配置文件
        CachingConnectionFactory cf = new CachingConnectionFactory("localhost");
        return cf;
    }

    @Bean(AMQP_ADMIN)
    public AmqpAdmin amqpAdmin(@Qualifier(C_F) ConnectionFactory cf) {
        RabbitAdmin admin = new RabbitAdmin(cf);
        return admin;
    }

    @Bean(SPRING_TEST_EXCHANGE)
    public Exchange springTestExchange() {
        return new TopicExchange("spring.test.exchange");
    }

    @Bean(SPRING_TEST_EXCHANGE_DLX)
    public Exchange springTestExchangeDlx() {
        return new TopicExchange("spring.test.exchange.dlx");
    }

    @Bean(SPRING_TEST_QUEUE)
    public Queue springTestQueue() {
        Queue queue = QueueBuilder
            .durable("spring.test.queue")
            .deadLetterExchange("spring.test.queue.dlx")
            .build();
        return queue;
    }

    @Bean(ID + "springTestQueue2SpringTestExchange")
    public Binding springTestQueue2SpringTestExchange(
        @Qualifier(SPRING_TEST_QUEUE) Queue queue,
        @Qualifier(SPRING_TEST_EXCHANGE) Exchange ex) {
        Binding bind = BindingBuilder
            .bind(queue)
            .to(ex)
            .with("#")
            .noargs();
        return bind;
    }

    @Bean
    public MessageListener exampleListener() {
        return message -> log.info("received: {}", message);
    }

}
