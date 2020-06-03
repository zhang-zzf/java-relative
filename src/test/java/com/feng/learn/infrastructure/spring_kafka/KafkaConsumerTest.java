package com.feng.learn.infrastructure.spring_kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ConsumerFactory.Listener;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

/**
 * @author zhanfeng.zhang
 * @date 2020/06/03
 */
public class KafkaConsumerTest {

    @Configuration
    @EnableKafka
    public class Config {

        @Bean
        ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
            ConcurrentKafkaListenerContainerFactory<String, String> factory =
                    new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            return factory;
        }

        @Bean
        public ConsumerFactory<String, String> consumerFactory() {
            Map<String, String> consumerConfigs = new HashMap<>();
            consumerConfigs.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            return new DefaultKafkaConsumerFactory(consumerConfigs);
        }

    }
}
