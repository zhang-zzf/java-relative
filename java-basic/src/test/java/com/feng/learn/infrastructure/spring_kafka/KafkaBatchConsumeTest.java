package com.feng.learn.infrastructure.spring_kafka;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

import com.feng.learn.infrastructure.spring_kafka.KafkaBatchConsumeTest.KafkaBatchConsumeConfig;
import com.feng.learn.infrastructure.spring_kafka.KafkaBatchConsumeTest.SomeTopicConsumer;
import java.util.HashMap;
import java.util.List;
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
    KafkaBatchConsumeConfig.class
})
public class KafkaBatchConsumeTest {

  /**
   * @version 2.5.1
   * <p>测试单线程批量消息消费</p>
   * <p>container thread => consumer.poll -> invoke SomeTopicConsumer.onMessage() </p>
   */
  @Ignore // 依赖环境，无法做自动化测试
  @Test
  public void test() throws InterruptedException {
    Thread.currentThread().join();
  }

  @Configuration
  public static class SomeTopicConsumer {

    @KafkaListener(id = "group_id", topics = "myTopic",
        containerFactory = KafkaBatchConsumeConfig.CONTAINER_FACTORY)
    public void onMessage(List<String> msg) {
      try {
        log.info("msg=> {}", msg);
      } catch (Exception e) {
        // very important => catch every exception
      }
    }
  }

  @Configuration
  @EnableKafka
  public static class KafkaBatchConsumeConfig {

    public static final String CONTAINER_FACTORY = "batchContainerFactory";

    @Bean(value = CONTAINER_FACTORY)
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
        ConsumerFactory consumerFactory) {
      ConcurrentKafkaListenerContainerFactory<String, String> factory =
          new ConcurrentKafkaListenerContainerFactory<>();
      factory.setConsumerFactory(consumerFactory);
      // import => 设置批量消费
      factory.setBatchListener(true);
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
