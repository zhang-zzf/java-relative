package com.feng.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
// 排除所有的aop bean扫描
@ComponentScan(excludeFilters = {
    @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "com.feng.learn.aop.*"
    )
})
//@MapperScan({"com.feng.learn.dao"}) // mybatis
public class SpringAopApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAopApplication.class, args);
    }
}
