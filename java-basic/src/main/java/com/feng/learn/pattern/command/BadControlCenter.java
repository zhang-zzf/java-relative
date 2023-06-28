package com.feng.learn.pattern.command;

import com.feng.learn.pattern.command.provider.Light;
import com.feng.learn.pattern.command.provider.TV;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author zhanfeng.zhang
 * @date 2019/12/31
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BadControlCenter {

  Object[] devices;

  public void onButtonPress(int index) {
    Object device = devices[index];
    String name = device.getClass().getSimpleName();
    if ("Light".equalsIgnoreCase(name)) {
      Light light = (Light) device;
      light.on();
    } else if ("TV".equalsIgnoreCase(name)) {
      TV tv = (TV) device;
      tv.on();
      tv.setVolume(50);
      tv.setInputChannel(0);
    }
    /**
     * ...... 无尽的else if
     */
    // 每次需求变动，都需要改动这里的代码
    // 问题出在调用方直接耦合被调用方，如Light/TV等
  }

  public void offButtonPress(int index) {
    Object device = devices[index];
    String name = device.getClass().getSimpleName();
    if ("Light".equalsIgnoreCase(name)) {
      Light light = (Light) device;
      light.off();
    } else if ("TV".equalsIgnoreCase(name)) {
      TV tv = (TV) device;
      tv.off();
    }
    /**
     * ...... 无尽的else if
     */
    // 每次需求变动，都需要改动这里的代码
  }
}
