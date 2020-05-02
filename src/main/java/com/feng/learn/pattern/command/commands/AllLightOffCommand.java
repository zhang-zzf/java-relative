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
 * @date 2019/12/31
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AllLightOffCommand implements Command {

    Light light;
    CeilingLight ceilingLight;
    OutdoorLight outdoorLight;

    @Override
    public void execute() {
        outdoorLight.off();
        light.off();
        ceilingLight.off();
    }

    @Override
    public void undo() {
        outdoorLight.on();
        light.on();
        ceilingLight.on();
    }
}
