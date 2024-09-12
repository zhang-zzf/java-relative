package com.feng.learn.ref;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AObject {

  @Override
  protected void finalize() throws Throwable {
    log.info("AObject was GC by the  JVM -> Object id: {}", objectHex(this));
  }

  public static String objectHex(Object obj) {
    return (String.format("0x%016X", Objects.hashCode(obj)));
  }

}
