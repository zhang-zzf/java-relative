package com.feng.learn.pattern.command;

import com.feng.learn.pattern.command.commands.NoCommand;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

/**
 * <p>CommandControlCenter 是命令的发起者，完全不感知谁去执行命令</p>
 * <p>不依赖什么电器类，实现了与电器类的解耦</p>
 * <p>当新增电器类（需求变更）时，只需要新增2个Command类，无需修改任何已经存在的类（面向扩展开放，面向修改封闭）</p>
 *
 * @author zhanfeng.zhang
 * @date 2019/12/31
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommandControlCenter {

    int slots = 7;

    Command[] onCommands = new Command[slots];
    Command[] offCommands = new Command[slots];

    @NonFinal
    private Command lastCommand;

    public CommandControlCenter() {
        NoCommand noCommand = new NoCommand();
        for (int i = 0; i < slots; i++) {
            onCommands[i] = noCommand;
            offCommands[i] = noCommand;
        }
        lastCommand = noCommand;
    }

    public void setCommandToSlot(int slot, Command onCommand, Command offCommand) {
        onCommands[slot] = onCommand;
        offCommands[slot] = offCommand;
    }

    public void onButtonPressed(int slot) {
        Command c = onCommands[slot];
        c.execute();
        lastCommand = c;
    }

    public void offButtonPressed(int slog) {
        Command c = offCommands[slog];
        c.execute();
        lastCommand = c;
    }

    public void undo() {
        lastCommand.undo();
    }
}
