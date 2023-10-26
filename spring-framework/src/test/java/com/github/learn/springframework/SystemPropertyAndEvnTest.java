package com.github.learn.springframework;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class SystemPropertyAndEvnTest {

  /**
   * 设置 property
   * {@link System#setProperty(String, String)}
   * {@link System#setProperties(Properties)}
   */
  @Test
  void givenSystem_whenGetProperty_then() {
    String pKey = "X-User-Id";
    String xUserId = "zhang.zzf";
    then(System.getProperty(pKey)).isNull();
    System.setProperty(pKey, xUserId);
    then(System.getProperty(pKey)).isEqualTo(xUserId);
  }

  /**
   * <pre>
   *    JVM 添加 -D 启动参数
   *    -ea -DX-User-Id=zhang.zzf
   * </pre>
   *
   */
  @Test
  @Disabled
  void givenJVMDArgs_whenGetProperty_then() {
    String pKey = "X-User-Id";
    String xUserId = "zhang.zzf";
    then(System.getProperty(pKey)).isEqualTo(xUserId);
  }


}