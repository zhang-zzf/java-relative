package com.feng.learn.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Trace {
  static final ThreadLocal<List<String>> TRACE = ThreadLocal.withInitial(ArrayList::new);

  public static void add(Object... args) {
    StackTraceElement trace = new Throwable().getStackTrace()[1];
    TRACE.get().add(trace.toString() + " -> args: " + Arrays.toString(args));
  }

  public static List<String> trace() {
    return TRACE.get();
  }

  public static String objectHex(Object obj) {
    return (String.format("0x%016X", Objects.hashCode(obj)));
  }

}
