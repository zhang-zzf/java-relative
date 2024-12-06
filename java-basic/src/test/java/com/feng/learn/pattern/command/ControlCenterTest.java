package com.feng.learn.pattern.command;

import com.feng.learn.pattern.command.provider.Light;
import com.feng.learn.pattern.command.provider.TV;
import org.junit.Test;

/**
 * @author zhanfeng.zhang
 * @date 2020/01/01
 */
public class ControlCenterTest {

    @Test
    public void buttonPressTest() {
        BadControlCenter center = new BadControlCenter(
            new Object[]{new Light("living room"), new TV()});
        center.onButtonPress(0);
        center.offButtonPress(1);
    }

}