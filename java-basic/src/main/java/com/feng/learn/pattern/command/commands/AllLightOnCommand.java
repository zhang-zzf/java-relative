package com.feng.learn.pattern.command.commands;

import com.feng.learn.pattern.command.Command;
import com.feng.learn.pattern.command.provider.CeilingLight;
import com.feng.learn.pattern.command.provider.Light;
import com.feng.learn.pattern.command.provider.OutdoorLight;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author zhanfeng.zhang
 * @date 2020-04-24
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AllLightOnCommand implements Command {

  Light light;
  CeilingLight ceilingLight;
  OutdoorLight outdoorLight;

  @Override
  public void execute() {
    outdoorLight.on();
    light.on();
    ceilingLight.on();
  }

  @Override
  public void undo() {
    outdoorLight.off();
    light.off();
    ceilingLight.off();
  }
}
