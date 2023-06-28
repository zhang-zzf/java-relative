package com.feng.learn.infrastructure.spring_kafka;

import static com.feng.learn.infrastructure.spring_kafka.KafkaBatchConsumeWithConverterTest.Config.CONTAINER_FACTORY;
import static com.feng.learn.infrastructure.spring_kafka.KafkaBatchConsumeWithConverterTest.Config.GROUP_ID;
import static com.feng.learn.infrastructure.spring_kafka.KafkaBatchConsumeWithConverterTest.Config.LISTENER_ERROR_HANDLER;
import static com.feng.learn.infrastructure.spring_kafka.KafkaBatchConsumeWithConverterTest.Config.TOPIC;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

import com.feng.learn.infrastructure.spring_kafka.KafkaBatchConsumeWithConverterTest.Config;
import com.feng.learn.infrastructure.spring_kafka.KafkaBatchConsumeWithConverterTest.Consumer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.BatchErrorHandler;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author zhanfeng.zhang
 * @date 2020/06/03
 */
@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    Consumer.class,
    Config.class
})
@Ignore
@Disabled
public class KafkaBatchConsumeWithConverterTest {

  /**
   * @version 2.5.1
   * <p>测试单线程批量消息消费</p>
   * <p>container thread => consumer.poll -> invoke SomeTopicConsumer.onMessage() </p>
   */
  //@Ignore // 依赖环境，无法做自动化测试
  @Test
  public void test() throws InterruptedException {
    Thread.currentThread().join();
  }

  @Configuration
  public static class Consumer {

    @KafkaListener(id = GROUP_ID,
        topics = TOPIC,
        containerFactory = CONTAINER_FACTORY,
        errorHandler = LISTENER_ERROR_HANDLER)
    public void onMessage(List<Message<JsonHolder>> msg) {
      log.info("msg=> {}", msg);
    }
  }

  @Configuration
  @EnableKafka
  public static class Config {

    public static final String GROUP_ID = "group_id";
    public static final String TOPIC = "myJsonTopic";
    public static final String CONTAINER_FACTORY = TOPIC + "ContainerFactory";
    public static final String CONSUMER_FACTORY = TOPIC + "ConsumerFactory";
    public static final String LISTENER_ERROR_HANDLER = TOPIC + "KafkaListenerErrorHandler";
    public static final String CONTAINER_ERROR_HANDLER = TOPIC + "BatchErrorHandler";

    @Bean(value = CONTAINER_FACTORY)
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
        @Qualifier(CONSUMER_FACTORY) ConsumerFactory consumerFactory,
        @Qualifier(CONTAINER_ERROR_HANDLER) BatchErrorHandler batchErrorHandler) {
      ConcurrentKafkaListenerContainerFactory<String, String> factory =
          new ConcurrentKafkaListenerContainerFactory<>();
      factory.setConsumerFactory(consumerFactory);
      // import => 设置批量消费
      factory.setBatchListener(true);
      // very important => 设置 converter
      factory.setMessageConverter(new BatchMessagingMessageConverter(new JsonMessageConverter()));
      // very important => 设置容器 errorHandler
      factory.setBatchErrorHandler(batchErrorHandler);
      return factory;
    }

    @Bean(CONSUMER_FACTORY)
    public ConsumerFactory consumerFactory() {
      Map<String, Object> consumerConfigs = new HashMap<>();
      consumerConfigs.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
      consumerConfigs.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
      consumerConfigs.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
      consumerConfigs.put(INTERCEPTOR_CLASSES_CONFIG,
          Arrays.asList(TraceConsumerInterceptor.class));
      return new DefaultKafkaConsumerFactory(consumerConfigs);
    }

    @Bean(LISTENER_ERROR_HANDLER)
    public KafkaListenerErrorHandler kafkaListenerErrorHandler() {
      return (Message<?> msg, ListenerExecutionFailedException exception) -> {
        log.error("kafka Message({}) deal exception {}", msg.getPayload(), exception);
        return null;
      };
    }

    @Bean(CONTAINER_ERROR_HANDLER)
    public BatchErrorHandler batchErrorHandler() {
      return (thrownException, data) -> {
        StringBuilder message = new StringBuilder("Error while processing:\n");
        if (data == null) {
          message.append("null ");
        } else {
          for (ConsumerRecord<?, ?> record : data) {
            message.append(record).append('\n');
          }
        }
        // 日志处理
        log.error(message.toString(), thrownException);
      };
    }

  }

  @Data
  public static class JsonHolder {

    String name;
    int age;
  }

  @Slf4j
  public static class TraceConsumerInterceptor implements ConsumerInterceptor {

    public static final String MSG_RECEIVE_TIME = "trace_msg_received_time";

    @Override
    public ConsumerRecords onConsume(final ConsumerRecords records) {
      log.info("after receive msg");
      if (records.isEmpty()) {
        return records;
      }
      Iterator<ConsumerRecord> it = records.iterator();
      while (it.hasNext()) {
        ConsumerRecord next = it.next();
      }
      return records;
    }

    @Override
    public void close() {

    }

    @Override
    public void onCommit(Map offsets) {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
  }
}



