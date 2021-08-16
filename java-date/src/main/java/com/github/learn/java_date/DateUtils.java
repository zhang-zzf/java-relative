package com.github.learn.java_date;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/05
 */
public class DateUtils {

    public static LocalDateTime convert(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDateTime getNow() {
        return LocalDateTime.now();
    }

    /**
     * 获取时间间隔
     *
     * @param start 开始时间
     * @param end 结束时间
     * @return 时间间隔（单位：秒）
     */
    public static long between(LocalDateTime start, LocalDateTime end) {
        Duration between = Duration.between(start, end);
        return between.getSeconds();
    }

    public static Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime toLocalDateTime(Date d) {
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime getEpoch() {
        return LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());
    }

    /**
     * String -> LocalDateTime
     *
     * @param value value
     * @param pattern pattern
     * @return data
     */
    public static LocalDateTime parse(String value, String pattern) {
        return LocalDateTime.parse(value, ofPattern(pattern));
    }

}
