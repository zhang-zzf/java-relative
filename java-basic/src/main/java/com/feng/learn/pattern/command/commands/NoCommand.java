package com.feng.learn.pattern.command.commands;


import com.feng.learn.pattern.command.Command;

/**
 * @author zhanfeng.zhang
 * @date 2019/12/31
 */
public class NoCommand implements Command {

  @Override
  public void execute() {
    // do nothing
  }

  @Override
  public void undo() {
    // still do nothing
  }
}
