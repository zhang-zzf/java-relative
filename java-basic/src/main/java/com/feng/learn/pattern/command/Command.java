package com.feng.learn.pattern.command;

/**
 * @author zhanfeng.zhang
 * @date 2019/12/31
 */
public interface Command {

    void execute();

    void undo();
}
