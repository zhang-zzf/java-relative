package com.github.learn.java_date;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
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
   * @param end   结束时间
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
   * @param value   value
   * @param pattern pattern
   * @return data
   */
  public static LocalDateTime parse(String value, String pattern) {
    return LocalDateTime.parse(value, ofPattern(pattern));
  }

  /**
   * String -> LocalDate
   *
   * @param value   value
   * @param pattern pattern
   * @return data
   */
  public static LocalDate parseLocalDate(String value, String pattern) {
    return LocalDate.parse(value, ofPattern(pattern));
  }

  /**
   * 时间转字符串
   *
   * @param localDateTime 时间
   * @param format        格式
   * @return data
   */
  public static String getDateString(LocalDateTime localDateTime, String format) {
    return localDateTime.format(DateTimeFormatter.ofPattern(format));
  }

  public static LocalDateTime endDayOf(LocalDateTime localDateTime) {
    return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
  }

  public static LocalDateTime startDayOf(LocalDateTime localDateTime) {
    return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN);
  }

  public static LocalDateTime endMonthOf(LocalDateTime localDateTime) {
    return endDayOf(localDateTime.with(TemporalAdjusters.lastDayOfMonth()));
  }

}
