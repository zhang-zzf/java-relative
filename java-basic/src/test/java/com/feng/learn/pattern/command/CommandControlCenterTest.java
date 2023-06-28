package com.feng.learn.pattern.command;

import com.feng.learn.pattern.command.commands.AllLightOffCommand;
import com.feng.learn.pattern.command.commands.AllLightOnCommand;
import com.feng.learn.pattern.command.commands.ComposeCommand;
import com.feng.learn.pattern.command.commands.LightOffCommand;
import com.feng.learn.pattern.command.commands.LightOnCommand;
import com.feng.learn.pattern.command.provider.CeilingLight;
import com.feng.learn.pattern.command.provider.Light;
import com.feng.learn.pattern.command.provider.OutdoorLight;
import org.junit.Test;

/**
 * @author zhanfeng.zhang
 * @date 2020/01/01
 */
public class CommandControlCenterTest {


  @Test
  public void testCommandPattern() {
    // center 是Invoker，会调用Command的execute()
    CommandControlCenter center = new CommandControlCenter();

    // Light 是命令的接受者
    Light light = new Light("living room");
    // Command 是命令，把命令的接受者和接受者如何执行命令封装起来
    Command lightOnCommand = new LightOnCommand(light);
    Command lightOffCommand = new LightOffCommand(light);

    center.setCommandToSlot(0, lightOnCommand, lightOffCommand);

    center.onButtonPressed(0);
    center.offButtonPressed(0);
  }

  @Test
  public void testACommandManyActions() {
    // center 是Invoker，会调用Command的execute()
    CommandControlCenter center = new CommandControlCenter();

    // Light 是命令的接受者
    Light light = new Light("living room");
    CeilingLight ceilingLight = new CeilingLight();
    OutdoorLight outdoorLight = new OutdoorLight();

    // Command 是命令，把命令的接受者和接受者如何执行命令封装起来
    AllLightOnCommand allLightOnCommand = new AllLightOnCommand(light, ceilingLight, outdoorLight);
    AllLightOffCommand allLightOffCommand = new AllLightOffCommand(light, ceilingLight,
        outdoorLight);

    center.setCommandToSlot(1, allLightOnCommand, allLightOffCommand);
    center.onButtonPressed(1);
    center.offButtonPressed(1);
  }

  @Test
  public void testComposeCommand() {
    CommandControlCenter center = new CommandControlCenter();
    Light livingRoomLight = new Light("living room");
    Light outDoorLight = new Light("out door");
    LightOnCommand livingRoomLightOn = new LightOnCommand(livingRoomLight);
    LightOnCommand outDoorLightOn = new LightOnCommand(outDoorLight);
    LightOffCommand livingRoomLightOff = new LightOffCommand(livingRoomLight);
    LightOffCommand outDoorLightOff = new LightOffCommand(outDoorLight);

    ComposeCommand allLightOn = new ComposeCommand(
        new Command[]{outDoorLightOn, livingRoomLightOn});
    ComposeCommand allLightOff = new ComposeCommand(
        new Command[]{outDoorLightOff, livingRoomLightOff});

    center.setCommandToSlot(0, allLightOn, allLightOff);

    center.onButtonPressed(0);
    center.undo();

    center.offButtonPressed(0);
    center.undo();
  }

}