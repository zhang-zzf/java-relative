package com.github.learn.springframework.autowired;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AServiceBTest {

  @Autowired
  AServiceB aServiceB;


  @Test
  void given_when_then() {
    String name = aServiceB.aServiceAA1Name();
    then(name).isEqualTo("aServiceAA1");
  }

}