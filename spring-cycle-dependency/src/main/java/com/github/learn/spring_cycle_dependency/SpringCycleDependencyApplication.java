package com.github.learn.spring_cycle_dependency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringCycleDependencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCycleDependencyApplication.class, args);
    }

}
