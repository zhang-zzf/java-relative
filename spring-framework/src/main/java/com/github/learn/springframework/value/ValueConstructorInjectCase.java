package com.github.learn.springframework.value;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Data
public class ValueConstructorInjectCase {
    /**
     * <pre>
     *
     * 1. final 配合 @RequiredArgsConstructor 自动生成构造器
     * 1. lombok.config 添加配置，自动把 @Value copy 到构造器上
     * </pre>
     */
    /**
     * <pre>
     *     public ValueConstructorInjectCase(@Value("${mysql.db.url:jdbc:mysql://localhost:3306}") final String dbUrl) {
     *         this.dbUrl = dbUrl;
     *     }
     * </pre>
     */
    @Value("${mysql.db.url:jdbc:mysql://localhost:3306}")
    final String dbUrl;
}
