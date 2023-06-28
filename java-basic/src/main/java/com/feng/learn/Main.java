package com.feng.learn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan({"com.feng.learn"})
public class Main {

  public static void main(String[] args) throws InterruptedException {
    ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
    log.info("app start success");
    Thread.currentThread().join();
  }

}
