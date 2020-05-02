package com.feng.learn.pattern.command.provider;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2019/12/31
 */
@Slf4j
public class CeilingLight {

    public void on() {
        log.info("ceilingLight on");
    }

    public void off() {
        log.info("ceilingLight off");
    }

    public void dim() {

    }
}
