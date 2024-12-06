package com.github.learn.snowflake_time.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/18
 */
@SpringBootTest
@Slf4j
class TimeBasedSnowFlakeTest {

    @Autowired
    TimeBasedSnowFlake timeBasedSnowFlake;

    @Test
    void test() {
        for (int i = 0; i < 10; i++) {
            final long id = timeBasedSnowFlake.generate();
            log.info("id: {}", id);
        }
    }

}