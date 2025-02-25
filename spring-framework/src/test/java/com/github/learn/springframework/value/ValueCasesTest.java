package com.github.learn.springframework.value;

import static org.assertj.core.api.BDDAssertions.then;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ValueCasesTest {
    @Autowired
    DataSource mysqlDataSource;
    @Autowired
    ValueConstructorInjectCase valueCases;
    @Autowired
    ConfigService configService;

    @Test
    void givenValueConstructorInject_when_then() {
        then(valueCases.getDbUrl())
            .isNotNull()
            .isEqualTo("jdbc:mysql://10.0.9.18:3306/")
        ;
    }

    @Test
    void givenConfigServiceInject_when_then() {
        then(configService.getValueFromConfig()).isNotNull();
        then(configService.getValueFromConfig2()).isEqualTo("a b c");
        then(configService.getValueFromConfigArr()).contains("a", "b", "c d");
        then(configService.getValueFromConfigArr2()).contains("a", "b", "c d");
        then(configService.getValueFromConfigArr21()).contains("a", "b", "c d");
        then(configService.getValueFromConfigArr22()).contains("a", "b", "c d");
        then(configService.getValueFromConfigArr3()).contains(1L, null, null);
        then(configService.getValueFromConfigArr4()).contains(1L, 2L);
        then(configService.getJsonString()).isEqualTo("[{\"name\":\"zhang.zzf\",\"age\":5,\"adult\":false}]");
        then(configService.getExceptionBundleList()).hasSize(2);
        then(configService.getMapValue()).hasSize(1);
        then(configService.getStrMapValue()).hasSize(3);
        then(configService.getKey1FromStrMap2()).isEqualTo("defaultValue1");
        then(configService.getConfigFromSystemEvn()).isNull();
        then(configService.getConfigFromSystemProperty()).isNotNull().isEqualTo("defaultVal");
    }

}