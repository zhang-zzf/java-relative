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
import java.util.Arrays;
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
class DateUtilsTest {


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

  @Test
  void givenDate_when_thenDemo() {
    Date date = new Date();
    // Fri Jan 03 22:46:55 CST 2020
    log.info("date: {}", date);

    long currentTimeMillis = System.currentTimeMillis();
    Date date2 = new Date(currentTimeMillis);

    long time = date2.getTime();
    boolean after = date2.after(date);

    then(time).isEqualTo(currentTimeMillis);
    then(after).isTrue();
  }

  /**
   * SimpleDateFormat 用于 DateString <=> Date 的相互转换
   */
  @Test
  void givenDateAndSimpleDateFormat_whenConvert_thenDemo() throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // 2024-08-30 14:29:52
    Date date = new Date(1724999392772L);
    String dateStr = format.format(date);
    then(dateStr).isEqualTo("2024-08-30 14:29:52");
    // then(format.parse(dateStr)).isEqualTo(date);
  }

  /**
   * Calendar 主要用于获取或更改 年月日时分秒 中的一个字段
   */
  @Test
  void givenCalendar_when_thenDemo() throws ParseException {
    Calendar instance = Calendar.getInstance();
    String dateStr = "2020-01-03 22:10:09";

    // Calendar <=> Date 相互转换
    Date now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
    instance.setTime(now);
    Date time = instance.getTime();
    then(time).isEqualTo(now);

    int year = instance.get(YEAR);
    then(year).isEqualTo(2020);
    // very important: the first month of the year is 0
    int month = instance.get(MONTH) + 1;
    then(month).isEqualTo(01);
    int dayOfMonth = instance.get(DAY_OF_MONTH);
    then(dayOfMonth).isEqualTo(03);

    // -1年
    instance.add(YEAR, -1);
    // +2小时
    instance.add(Calendar.HOUR, +2);

    Calendar.getInstance(TimeZone.getTimeZone("China/Shanghai"), Locale.CHINA);
  }

  @Test
  void givenInstant_when_then() {
    log.info("{}", Instant.now());
  }

  @Test
  void givenLocalDate_when_then() {
    // 当前时间
    LocalDate now = LocalDate.now();
    log.info("LocalDate: {}", now);
    log.info("LocalDate => {}-{}-{}", now.getYear(), now.getMonthValue(), now.getDayOfMonth());

    LocalDate of = LocalDate.of(2020, 2, 1);
    log.info("LocalDate.of(): {}", of);

    LocalDate ofYearDay = LocalDate.ofYearDay(2020, 1);
    log.info("LocalDate.ofYearDay(): {}", ofYearDay);

    LocalDate parse = LocalDate.parse("2020-01-04");
    log.info("LocalDate.parse(): {}", parse);
    log.info("LocalDate.now() + 1 week: {}", parse.plusWeeks(1));
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
    log.info("period=>{}, year=>{}, month=>{}, day=>{}", period, period.getYears(), period.getMonths(), period.getDays());
  }

  @Test
  void givenDuration_when_then() {
    Duration oneDay = Duration.of(1, ChronoUnit.DAYS);
    log.info("Duration: {}", oneDay);
    then(oneDay.get(ChronoUnit.SECONDS)).isEqualTo(24 * 60 * 60);
  }

  @Test
  void givenTimeZone_when_then() {
    TimeZone aDefault = TimeZone.getDefault();
    log.info("TimeZone default: {}", aDefault);
    String[] availableIDs = TimeZone.getAvailableIDs(8 * 60 * 60 * 1000);
    Arrays.stream(availableIDs).forEach(System.out::println);

    Clock systemUTC = Clock.systemUTC();
    log.info("Clock.systemUTC(): {}", systemUTC);

    ZoneId defaultZoneId = ZoneId.systemDefault();
    log.info("defaultZoneId: {}", defaultZoneId);
    ZoneId zoneIdOfShanghai = ZoneId.of("Asia/Shanghai");
    log.info("zoneIdOfShanghai: {}", zoneIdOfShanghai);
    Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
    log.info("availableZoneIds: {}", availableZoneIds);
  }
}