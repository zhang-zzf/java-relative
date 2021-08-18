package com.github.learn.snowflake_time.domain.service;

import static java.time.temporal.ChronoField.MILLI_OF_SECOND;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/18
 */
@Service
@Slf4j
public class TimeBasedSnowFlake {

    /**
     * generate id use time
     * <p>不保证 id 不重复</p>
     * <p>${yyyyMMddHHmmss} * 100000 + ${ms} + new Random().nextInt(99000)</p>
     * <p>sample: 202108182202566288</p>
     *
     * @return id
     */
    public long generate() {
        final LocalDateTime now = LocalDateTime.now();
        final String time = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(now);
        final long id = Long.parseLong(time) * 100000 + now.get(MILLI_OF_SECOND) + new Random().nextInt(99000);
        return id;
    }

}
