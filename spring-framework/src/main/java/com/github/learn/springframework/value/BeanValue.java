package com.github.learn.springframework.value;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BeanValue {
    @Bean
    public DataSource mysqlDataSource(
        @Value("${mysql.db.url:jdbc:mysql://localhost:3306}") String url,
        @Value("${mysql.db.username:root}") String username,
        @Value("${mysql.db.password:test}") String password) {
        log.info("{}, {}, {}", url, username, password);
        return null;
    }
}
