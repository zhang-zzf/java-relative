package com.github.learn.springframework.value;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ValueCasesTest {
    @Autowired
    DataSource mysqlDataSource;
    @Autowired
    ValueCases valueCases;


    @Test
    void given_when_then() {
        valueCases.logDbUrl();
    }

}