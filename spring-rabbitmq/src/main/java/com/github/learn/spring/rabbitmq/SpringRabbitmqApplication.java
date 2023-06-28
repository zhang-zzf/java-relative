package com.github.learn.spring.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

@SpringBootApplication(exclude = RabbitAutoConfiguration.class)
public class SpringRabbitmqApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringRabbitmqApplication.class, args);
  }

}
