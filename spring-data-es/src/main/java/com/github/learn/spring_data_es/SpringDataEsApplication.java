package com.github.learn.spring_data_es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;

@SpringBootApplication(exclude = {ElasticsearchRestClientAutoConfiguration.class})
public class SpringDataEsApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringDataEsApplication.class, args);
  }

}
