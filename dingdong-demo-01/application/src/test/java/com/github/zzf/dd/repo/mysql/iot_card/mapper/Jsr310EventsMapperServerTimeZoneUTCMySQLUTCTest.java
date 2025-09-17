package com.github.zzf.dd.repo.mysql.iot_card.mapper;

import static org.assertj.core.api.Java6BDDAssertions.then;

import com.github.zzf.dd.repo.mysql.iot_card.entity.Jsr310Events;
import io.lettuce.core.BitFieldArgs.Offset;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-09-11
 *  * JVM Asia/Shanghai
 *  * serverTimeZone UTC
 *  * MySQL UTC
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("serverTimeZoneUTC_MySQLUTC")
@Slf4j
public class Jsr310EventsMapperServerTimeZoneUTCMySQLUTCTest {

    @Autowired
    Jsr310EventsMapper eventsMapper;

    /**
     * 结论：
     * <pre>
     *     1. Instant 以 serverTimeZone 设置的时区转换成 MySQL 协议的挂钟时间，和 JVM 的时区无关
     *     1. LocalDateTime (本身无时区信息 2025-09-15T08:00:00）直接 = MySQL 协议的挂钟时间，和 JVM 的时区无关
     *     1. mysql:mysql-connector-java:8.0.22 下无法保存 ZonedDateTime
     *     1. mysql:mysql-connector-java:8.0.22 下无法保存 OffsetDateTime
     * </pre>
     */
    @Test
    public void givenMySQL_whenInsertAndSelect_then() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Riyadh"));
        then(TimeZone.getDefault().getID()).isEqualTo("Asia/Riyadh");
        then(ZoneId.systemDefault().getId()).isEqualTo("Asia/Riyadh");
        Jsr310Events e1 = new Jsr310Events();
        // LocalDateTime -> text: 2025-09-15 08:00:00 (MySQL 协议) -> 2025-09-15 08:00:00 (datetime MySQL 原封保存)
        e1.setDsLocalTime(LocalDateTime.parse("2025-09-15T08:00:00"));
        e1.setTz("Asia/Shanghai");
        //
        // Instant 和时区无关，世界时间 ， unix timestamp
        // Instant 2025-09-15T15:00:00Z
        // -> 按照 serverTimeZone(UTC) 设置的时区转换成挂钟时间 text: 2025-09-15 15:00:00 (MySQL 协议)
        // -> 2025-09-15 08:00:00 (datetime MySQL 原封保存)
        Instant instant = Instant.parse("2025-09-15T15:00:00Z");
        e1.setDsUtcTime(instant);
        e1.setTsUtcTime(instant);
        //
        // ZonedDateTime 2025-09-15T23:00:00+08:00[Asia/Shanghai] -> 2025-09-15T15:00:00Z
        ZonedDateTime tsUtcTime = instant.atZone(ZoneId.of("Asia/Shanghai"));
        // mysql:mysql-connector-java:8.0.22 下无法保存 ZonedDateTime
        // Caused by: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Incorrect datetime value: '\xAC\xED\x00\x05sr\x00\x0Djava.time.Ser\x95]\x84\xBA\x1B"H\xB2\x0C\x00\x00xpw\x19\x06\x00\x00\x07\xE9\x09\x0F\xE8 \x07\x00\x0DAs' for column 'ts_utc_time' at row 1
        // e1.setTsUtcTime(tsUtcTime);
        //
        // OffsetDateTime 2025-09-15T23:00:00+08:00 -> 2025-09-15T15:00:00Z
        OffsetDateTime offsetDateTime = instant.atOffset(ZoneOffset.of("+08:00"));
        // mysql:mysql-connector-java:8.0.22 下无法保存 OffSetDateTime
        // Caused by: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Incorrect datetime value: '\xAC\xED\x00\x05sr\x00\x0Djava.time.Ser\x95]\x84\xBA\x1B"H\xB2\x0C\x00\x00xpw\x09\x0A\x00\x00\x07\xE9\x09\x0F\xE8 x' for column 'ts_utc_time' at row 1
        // e1.setTsUtcTime(offsetDateTime);
        int row = eventsMapper.insert(e1);
        Jsr310Events events = eventsMapper.selectLatest();
        then(events).isNotNull();
    }
}