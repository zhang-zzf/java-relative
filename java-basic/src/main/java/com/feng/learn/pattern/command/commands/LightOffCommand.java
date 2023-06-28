package com.feng.learn.pattern.command.commands;

import com.feng.learn.pattern.command.Command;
import com.feng.learn.pattern.command.provider.Light;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author zhanfeng.zhang
 * @date 2019/12/31
 */
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LightOffCommand implements Command {

  Light light;

  @Override
  public void execute() {
    light.off();
  }

  @Override
  public void undo() {
    light.on();
  }
}
