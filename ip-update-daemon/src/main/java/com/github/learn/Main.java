package com.github.learn;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhang.zzf
 * @date 2020-04-18
 */
@Slf4j
@Configuration
@ComponentScan
public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(Main.class);
        log.info("App start success.");
        Thread.currentThread().join();
    }

}
