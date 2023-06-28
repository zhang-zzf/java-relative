package com.feng.learn.pattern.command.commands;

import com.feng.learn.pattern.command.Command;
import com.feng.learn.pattern.command.provider.Stereo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author zhanfeng.zhang
 * @date 2019/12/31
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class StereoOnCommand implements Command {

  Stereo stereo;

  @Override
  public void execute() {
    stereo.on();
    stereo.setCd();
    stereo.setVolume(11);
  }

  @Override
  public void undo() {
    stereo.off();
  }
}
