package com.feng.learn.infrastructure.spring_kafka;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

import com.feng.learn.infrastructure.spring_kafka.KafkaConsumeTest.KafkaConsumerConfig;
import com.feng.learn.infrastructure.spring_kafka.KafkaConsumeTest.SomeTopicConsumer;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhanfeng.zhang
 * @date 2020/06/03
 */
@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    SomeTopicConsumer.class,
    KafkaConsumerConfig.class
})
public class KafkaConsumeTest {

  /**
   * @version 2.5.1
   * <p>测试单线程单消息消费</p>
   * <p>container thread => consumer.poll -> invoke SomeTopicConsumer.onMessage() for each msg
   * in List<msg></p>
   */
  @Ignore // 依赖环境，无法做自动化测试
  @Test
  public void testSingleMsg() throws InterruptedException {
    Thread.currentThread().join();
  }

  @Configuration
  public static class SomeTopicConsumer {

    @KafkaListener(id = "group_id", topics = "test",
        containerFactory = KafkaConsumerConfig.CONTAINER_FACTORY)
    public void onMessage(String msg) {
      log.info("msg=> {}", msg);
    }
  }

  @Configuration
  @EnableKafka
  public static class KafkaConsumerConfig {

    public static final String CONTAINER_FACTORY = "concurrentKafkaListenerContainerFactory";

    @Bean(value = CONTAINER_FACTORY)
    ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerContainerFactory(
        ConsumerFactory consumerFactory) {
      ConcurrentKafkaListenerContainerFactory<String, String> factory =
          new ConcurrentKafkaListenerContainerFactory<>();
      factory.setConsumerFactory(consumerFactory);
      ContainerProperties containerProperties = factory.getContainerProperties();
      return factory;
    }

    @Bean
    public ConsumerFactory consumerFactory() {
      Map<String, Object> consumerConfigs = new HashMap<>();
      consumerConfigs.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
      consumerConfigs.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
      consumerConfigs.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
      return new DefaultKafkaConsumerFactory(consumerConfigs);
    }

  }
}
