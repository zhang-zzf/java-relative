package com.github.learn.springframework.value;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanValue {
  @Bean
  public DataSource mysqlDataSource(
      @Value("mysql.db.url") String url,
      @Value("mysql.db.username") String username,
      @Value("mysql.db.password") String password) {
    return null;
  }
}
