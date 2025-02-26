package com.github.learn;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhang.zzf
 * @date 2020-04-18
 */
@Slf4j
public class Application {

    public static void main(String[] args) {
        log.info("starting Application...");
        new ThreadRamLocation().startThread(args);
        log.info("Application started");
    }

}
