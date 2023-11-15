package com.github.learn.springframework.autowired;

import static org.assertj.core.api.BDDAssertions.then;

import com.github.learn.springframework.converter.ConvertServiceDemo;
import com.github.learn.springframework.converter.ConvertServiceDemo2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConvertServiceDemoTest {

  @Autowired
  ConvertServiceDemo convertServiceDemo;


  @Test
  void givenSpringIoC_when_then() {
    String name = convertServiceDemo.convertToString(1);
    then(name).isEqualTo("1");
  }

}