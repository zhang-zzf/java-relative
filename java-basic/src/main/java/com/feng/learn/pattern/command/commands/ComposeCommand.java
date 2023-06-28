package com.feng.learn.pattern.command.commands;

import com.feng.learn.pattern.command.Command;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author zhanfeng.zhang
 * @date 2020/01/01
 */
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ComposeCommand implements Command {

  Command[] commands;

  @Override
  public void execute() {
    Arrays.stream(commands).forEach(Command::execute);
  }

  @Override
  public void undo() {
    Arrays.stream(commands).forEach(Command::undo);
  }
}
