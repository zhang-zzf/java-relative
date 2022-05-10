package com.github.learn;

import com.github.learn.thread.state.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhang.zzf
 * @date 2020-04-18
 */
@Slf4j
public class Application {

    public static void main(String[] args) {
        log.info("starting application...");
        new NewState().validate();
        new RunnableState().validate();
        new BlockedState().validate();
        new WaitingState().validate();
        new TimedWaitingState().validate();
        new TerminatedState().validate();
        log.info("application started");
    }

}
