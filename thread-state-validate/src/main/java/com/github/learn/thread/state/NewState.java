package com.github.learn.thread.state;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2022/05/10
 */
@Slf4j
public class NewState {

    public void validate() {
        //noinspection AlibabaAvoidManuallyCreateThread
        Thread t = new Thread(() -> log.info("thread-state-new"), "thread-state-NEW");
        log.info("Thread: {} -> state: {}", t, t.getState());
    }

}
