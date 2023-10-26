package com.github.learn.springframework;

import static java.lang.System.out;

public class SystemEnv {
  public static void main(String[] args) {
    out.println("Env_Shell_Version = " + System.getenv("Env_Shell_Version"));
  }
}
