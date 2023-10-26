package com.github.learn.springframework.autowired;

import com.github.learn.springframework.autowired.AServiceAABeans.AServiceAA;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AServiceB {

  /**
   * <pre>
   *   构造器注入
   *   可以正常注入
   *   {@link AServiceAABeans }定义了名字为 aServiceAA1 的 bean
   * </pre>
   */
  final AServiceAA aServiceAA1;
  AServiceAA aServiceAA2;

  public String aServiceAA1Name() {
    return aServiceAA1.name();
  }

  public String aServiceAA2Name() {
    return aServiceAA2.name();
  }

  /**
   * <pre>
   * 可以正常注入
   * {@link AServiceAABeans }定义了名字为 aServiceAA2 的 bean
   * </pre>
   */
  @Autowired
  final void setAServiceAA2(AServiceAA aServiceAA2) {
    this.aServiceAA2 = aServiceAA2;
  }

}
