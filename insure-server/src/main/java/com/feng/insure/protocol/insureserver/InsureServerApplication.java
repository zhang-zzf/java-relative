package com.feng.insure.protocol.insureserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class InsureServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(InsureServerApplication.class, args);
  }

}
