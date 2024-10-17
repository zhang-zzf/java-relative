package com.github.learn.java_date;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/05
 */
@Slf4j
class JSR310DateTest {


    @Test
    public void givenNow_whenEndMonth_then() {
        final LocalDateTime endMonthOf = DateUtils.endMonthOf(LocalDateTime.now());
        then(endMonthOf).isNotNull();
    }

    @Test
    public void givenyyyyMMdd_whenParse_thenSuccess() {
        final LocalDate parsed = DateUtils.parseLocalDate("19880329", "yyyyMMdd");
        then(parsed).isNotNull();
    }

    /**
     * 格式必须一一对应
     * <p>2021-03-26 14:37:29.0</p>
     * <p>yyyy-MM-dd HH:mm:ss.S</p>
     */
    @Test
    void givenString_whenConvertToLocalDateTime_then() {
        String value = "2021-03-26 14:37:29.0";
        final LocalDateTime localDateTime = DateUtils.parse(value, "yyyy-MM-dd HH:mm:ss.S");
        then(localDateTime).isNotNull();
    }

    @Test
    void givenTime_whenConvertToString_then() {
        String value = "2021-03-26 14:37:29.0";
        final LocalDateTime localDateTime = DateUtils.parse(value, "yyyy-MM-dd HH:mm:ss.S");
        final String s = DateUtils.getDateString(localDateTime, "yyyyMMddHHmmss");
        then(s).isEqualTo("20210326143729");
    }

    @Test
    void givenFile_whenTemp_then() throws IOException {
        for (int i = 0; i < 10; i++) {
            final File tempFile = File.createTempFile("[data]", ".dat");
            log.info("file: {}", tempFile.getAbsolutePath());
        }
    }

    /**
     * Calendar 主要用于获取或更改 年月日时分秒 中的一个字段
     */
    @Test
    void givenCalendar_when_thenDemo() throws ParseException {
        Calendar instance = Calendar.getInstance();
        // Calendar <=> Date 相互转换
        Date now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-01-03 22:10:09");
        instance.setTime(now);
        Date time = instance.getTime();
        then(time).isEqualTo(now);
        //
        int year = instance.get(YEAR);
        then(year).isEqualTo(2020);
        // very important: the first month of the year is 0
        int month = instance.get(MONTH) + 1;
        then(month).isEqualTo(01);
        int dayOfMonth = instance.get(DAY_OF_MONTH);
        then(dayOfMonth).isEqualTo(03);
        //
        // -1年
        instance.add(YEAR, -1);
        // +2小时
        instance.add(Calendar.HOUR, +2);
        //
        Calendar.getInstance(TimeZone.getTimeZone("China/Shanghai"), Locale.CHINA);
    }

    /**
     * Instant 表示 unix timestamp
     * <p>和时区无关</p>
     */
    @Test
    void givenInstant_when_then() {
        // 当前时区(Asia/Shanghai)的时间: 2024-10-16T09:39:11.760336
        // 2024-10-16T01:39:11.760336Z
        log.info("{}", Instant.now());
        Instant utcZero = Instant.EPOCH;
        // to timestamp
        then(utcZero.toEpochMilli()).isEqualTo(0L);
        // to String
        Instant utc0 = Instant.ofEpochMilli(0L);
        then(utc0.toString()).isEqualTo("1970-01-01T00:00:00Z");
        Instant _1ns = Instant.ofEpochSecond(0L, 1L);
        then(_1ns.toString()).isEqualTo("1970-01-01T00:00:00.000000001Z");
        //
        // to Zone relative time
        // 1970-01-01T08:00+08:00[Asia/Shanghai]
        ZonedDateTime zonedDateTime = Instant.EPOCH.atZone(ZoneId.of("Asia/Shanghai"));
        then(zonedDateTime.toString()).isEqualTo("1970-01-01T08:00+08:00[Asia/Shanghai]");
        then(zonedDateTime.toLocalDateTime().toString()).isEqualTo("1970-01-01T08:00");
        then(zonedDateTime.toLocalDate().toString()).isEqualTo("1970-01-01");
        then(zonedDateTime.toLocalTime().toString()).isEqualTo("08:00");
        then(zonedDateTime.getZone().getId()).isEqualTo("Asia/Shanghai");
    }

    /**
     * LocalDate / LocalDateTime / LocalTime 是和当前时区绑定的当地时间
     */
    @Test
    void givenLocalDate_when_then() {
        // 当前时间
        LocalDate now = LocalDate.now();
        log.info("LocalDate: {}", now);
        log.info("LocalDate => {}-{}-{}", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        //
        LocalDate of = LocalDate.of(2020, 2, 1);
        then(of.toString()).isEqualTo("2020-02-01");
        //
        LocalDate ofYearDay = LocalDate.ofYearDay(2020, 1);
        then(ofYearDay.toString()).isEqualTo("2020-01-01");
        //
        LocalDate parse = LocalDate.parse("2020-01-04");
        then(parse.toString()).isEqualTo("2020-01-04");
        log.info("LocalDate.now() + 1 week: {}", parse.plusWeeks(1));
        //
        //
        LocalDate epoch = LocalDate.of(1970, 1, 1);
        // LocalDateTime localDateTime = epoch.atStartOfDay();
        // todo
        // then(localDateTime.toInstant(ZoneOffset.of("Asia/Shanghai")).toEpochMilli()).isEqualTo(-28800000L);

    }

    @Test
    void givenMonthDay_when_then() {
        LocalDate myBirthDay = LocalDate.of(1988, 03, 29);
        log.info("LocalDate: {}", myBirthDay);
        MonthDay monthDay = MonthDay.from(myBirthDay);
        log.info("MonthDay: {}", monthDay);
        LocalDate localDate = LocalDate.of(2019, 03, 29);
        boolean b = monthDay.equals(MonthDay.from(localDate));
        then(b).isTrue();
    }

    @Test
    void givenLocalTime_when_then() {
        LocalTime now = LocalTime.now();
        log.info("LocalTime: {}", now);
        then(now.plusHours(-1)).isEqualTo(now.minusHours(1));
        then(now.plus(-1, ChronoUnit.HOURS)).isEqualTo(now.minus(Duration.ofHours(1)));
    }

    @Test
    void givenLocalDateTime_when_then() {
        LocalDateTime now = LocalDateTime.now();
        log.info("LocalDateTime: {}", now);
        ZoneId zoneIdOfNY = ZoneId.of("America/New_York");
        LocalDateTime newYork = LocalDateTime.now(zoneIdOfNY);
        log.info("newYork: {}", newYork);

        Instant instant = Instant.now();
        log.info("Instant: {}", instant);
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);

        log.info("Asia/Shanghai LocalDateTime: {}", localDateTime);
        log.info("instant=>{}, zoneOfShanghai=>{}", instant, localDateTime.toInstant(ZoneOffset.UTC));
        log.info("instant=>{}, zoneOfShanghai=>{}", instant.toEpochMilli(),
            localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());

        ZonedDateTime zonedDateTime = now.atZone(zoneIdOfNY);
        log.info("ZoneDateTime: {}", zonedDateTime);
        log.info("ZoneDateTime.toLocalDateTime(): {}", zonedDateTime.toLocalDateTime());
    }

    @Test
    void givenPeriod_when_then() {
        Period of = Period.of(1, 1, 1);
        LocalDate now = LocalDate.now();
        LocalDate future = now.plusYears(1).plusMonths(11).plusDays(66);
        log.info("now=>{}, future=>{}", now, future);
        Period period = Period.between(now, future);
        log.info("period=>{}, year=>{}, month=>{}, day=>{}", period, period.getYears(), period.getMonths(),
            period.getDays());
    }

    @Test
    void givenDuration_when_then() {
        Duration oneDay = Duration.of(1, ChronoUnit.DAYS);
        log.info("Duration: {}", oneDay);
        then(oneDay.get(ChronoUnit.SECONDS)).isEqualTo(24 * 60 * 60);
    }


    /**
     * 时区有关
     *
     * <pre>
     *     A ZoneId is used to identify the rules used to convert between an Instant and a LocalDateTime
     *     There are two distinct types of ID:
     *        1. Fixed offsets - a fully resolved offset from UTC/Greenwich, that uses the same offset for all local date-times
     *        2. Geographical regions - an area where a specific set of rules for finding the offset from UTC/Greenwich apply
     *     ZoneId 可以使用以下2种表达
     *        1. UTC 偏移量，固定值。使用 ZoneOffset 表达
     *        2. 时区
     *     时区和偏移量的区别
     *     Different parts of the world have different time-zone offsets. The rules for how offsets vary by place and time of year are captured in the ZoneId class.
     *     For example, Paris is one hour ahead of Greenwich/UTC in winter and two hours ahead in summer. The ZoneId instance for Paris will reference two ZoneOffset instances - a +01:00 instance for winter, and a +02:00 instance for summe
     * </pre>
     * <pre>
     *      ZoneId 的构建与 TimeZone 一致，时区名字可以使用以下3中方式
     *      1. Asia/Shanghai , UTC
     *      1. GMT+08:00 固定偏移量 / UTC+08:00 固定偏移量
     *      1. +08:00
     *
     * </pre>
     */
    @Test
    void givenZone_when_then() {
        //
        TimeZone.setDefault(TimeZone.getTimeZone("UTC")); // 默认 UTC 时区
        then(ZoneId.systemDefault().getId()).isEqualTo("UTC");
        // from String
        // 1. region-based IDs
        then(ZoneId.of("Asia/Shanghai").getId()).isEqualTo("Asia/Shanghai");
        // 2. The simplest type of ID is that from ZoneOffset. This consists of 'Z' and IDs starting with '+' or '-'.
        then(ZoneId.of("Z").normalized()).isInstanceOf(ZoneOffset.class);
        then(ZoneId.of("+08:00").normalized()).isInstanceOf(ZoneOffset.class);
        then(ZoneId.of("+08:00").getId()).isEqualTo("+08:00");
        // 3. offset-style IDs with some form of prefix, such as 'GMT+2' or 'UTC+01:00'. The recognised prefixes are 'UTC', 'GMT' and 'UT'
        then(ZoneId.of("GMT+08:00").getId()).isEqualTo("GMT+08:00");
        // GMT+08:00 被解析成固定 offset
        then(ZoneId.of("UTC+0").getId()).isEqualTo("UTC");
        then(ZoneId.of("UTC+00:00").getId()).isEqualTo("UTC");
        then(ZoneId.of("GMT+0").getId()).isEqualTo("GMT");
        then(ZoneId.of("GMT+08").normalized()).isInstanceOf(ZoneOffset.class);
        then(ZoneId.of("GMT+08:00").normalized()).isInstanceOf(ZoneOffset.class);
        then(ZoneId.of("UTC+08").normalized()).isInstanceOf(ZoneOffset.class);
        then(ZoneId.of("UTC+08:00").normalized()).isInstanceOf(ZoneOffset.class);
        then(ZoneId.of("UTC+0800").normalized()).isInstanceOf(ZoneOffset.class);
        // 时区的 ZoneId 不能转换成 ZoneOffset
        // java.time.ZoneRegion
        then(ZoneId.of("Asia/Shanghai").normalized()).isNotInstanceOf(ZoneOffset.class);
        //
        // ZoneOffset
        then(ZoneOffset.UTC.getId()).isEqualTo("Z");
        then(ZoneOffset.ofHoursMinutes(8, 0).getId()).isEqualTo("+08:00");
        log.info("offset -> utc: {}, z8: {}", ZoneOffset.UTC, ZoneOffset.ofHoursMinutes(8, 0));
        // from String
        // "Z" == "GMT" "UTC" "GMT+0" "UTC+0"
        then(ZoneOffset.of("Z").getTotalSeconds()).isEqualTo(0L);
        then(ZoneOffset.of("+0").getId()).isEqualTo("Z");
        then(ZoneOffset.of("+00").getId()).isEqualTo("Z");
        then(ZoneOffset.of("+00:00").getId()).isEqualTo("Z");
        then(ZoneOffset.of("+00:00:00").getId()).isEqualTo("Z");
        then(ZoneOffset.of("+08:00").getTotalSeconds()).isEqualTo(28800L);
        // Invalid ID for ZoneOffset, non numeric characters found: GMT+00
        // then(ZoneOffset.of("GMT+0").getId()).isEqualTo("Z");
        // Invalid ID for ZoneOffset, non numeric characters found: UTC+00
        // then(ZoneOffset.of("UTC+00").getId()).isEqualTo("Z");
        // 获取可用时区ID
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        log.info("availableZoneIds: {}", availableZoneIds);
    }


    @Test
    void givenClock_when_then() {
        Clock systemUTC = Clock.systemUTC();
        log.info("Clock.systemUTC(): {}", systemUTC);
    }
}