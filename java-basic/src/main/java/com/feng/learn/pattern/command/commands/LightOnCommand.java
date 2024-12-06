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
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class LightOnCommand implements Command {

    Light light;

    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        light.off();
    }
}
