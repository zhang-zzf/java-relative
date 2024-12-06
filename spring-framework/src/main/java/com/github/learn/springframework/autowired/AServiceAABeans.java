package com.github.learn.springframework.autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AServiceAABeans {

    interface AServiceAA {
        String name();
    }

    @Bean
    public AServiceAA aServiceAA1() {
        return () -> "aServiceAA1";
    }

    @Bean
    public AServiceAA aServiceAA2() {
        return () -> "aServiceAA2";
    }
}
