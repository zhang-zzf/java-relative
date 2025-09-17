package com.github.zzf.dd.repo.mysql.iot_card.mapper;

import static org.assertj.core.api.Java6BDDAssertions.then;

import com.github.zzf.dd.repo.mysql.iot_card.entity.Events;
import com.github.zzf.dd.repo.mysql.iot_card.entity.Jsr310Events;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-09-11
 *  * JVM Asia/Shanghai
 *  * serverTimeZone Asia/Shanghai
 *  * MySQL Asia/Shanghai
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class Jsr310EventsMapperTest {

    @Autowired
    Jsr310EventsMapper eventsMapper;

    @Test
    public void givenJVMUTCMySQL_whenInsertAndSelect_then() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        then(TimeZone.getDefault().getID()).isEqualTo("UTC");
        then(ZoneId.systemDefault().getId()).isEqualTo("UTC");
        //
        Jsr310Events e1 = new Jsr310Events();
        Instant instant = Instant.now();
        e1.setDsLocalTime(LocalDateTime.now());
        e1.setTz("Asia/Shanghai");
        e1.setDsUtcTime(instant);
        ZonedDateTime tsUtcTime = instant.atZone(ZoneId.of("Asia/Shanghai"));
        e1.setTsUtcTime(instant);
        // 按照 serverTimeZone 把 Instant 转换成挂钟时间，和 JVM 时区无关
        // Statement: insert into events (ds_local_time, tz, ds_utc_time, ts_utc_time)
        // values ('2025-09-16 13:31:25.004359', 'Asia/Shanghai', '2025-09-16 21:31:25.004256', '2025-09-16 21:31:25.004256')
        int row = eventsMapper.insert(e1);
        Jsr310Events events = eventsMapper.selectLatest();
        then(events).isNotNull();
    }

    @Test
    public void givenMySQL_whenInsertAndSelect_then() {
        Jsr310Events e1 = new Jsr310Events();
        Instant instant = Instant.now();
        e1.setDsLocalTime(LocalDateTime.now());
        e1.setTz("Asia/Shanghai");
        e1.setDsUtcTime(instant);
        e1.setTsUtcTime(instant);
        int row = eventsMapper.insert(e1);
        Jsr310Events events = eventsMapper.selectLatest();
        then(events).isNotNull();
    }
    
}