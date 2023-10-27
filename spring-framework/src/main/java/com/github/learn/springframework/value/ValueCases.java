package com.github.learn.springframework.value;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValueCases {
  @Value("${mysql.db.url:jdbc:mysql://localhost:3306}")
  final String dbUrl;

  public void logDbUrl() {
    log.info("{}", dbUrl);
  }
}
