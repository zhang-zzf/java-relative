package com.github.learn.springframework.resource_annotation;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-02-26
 */
@SpringBootTest
class AServiceTest {

    @Autowired
    private AService aService;

    /**
     * UT 要求
     * 1. 方法中不要有空行
     * 1. 使用 BDD 风格的描述来编写测试
     * 1. 测试方法名使用下划线分隔 given、when 和 then 部分，而 given、when 和 then 之间的部分使用驼峰命名法。
     * 1. 使用 assertJ 的 BDD 语法写断言，断言时，使用 then(result).isEqualTo(expected)
     */

    @Test
    @DisplayName("Given AService is initialized, when bServiceName is called, then it should return the name from BServiceImpl")
    public void given_AServiceIsInitialized_when_BServiceNameIsCalled_then_ItShouldReturnTheNameFromBServiceImpl() {
        // Arrange
        String expectedName = "bServiceImpl";
        // Act
        String result = aService.bServiceName();
        // Assert
        then(result).isEqualTo(expectedName);
    }

}