package com.github.learn.java_date;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

}