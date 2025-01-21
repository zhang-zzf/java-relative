package com.github.zzf.learn.rpc.http.provider.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-01-09
 */
@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {

    @GetMapping("/demo")
    public void demo() {
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
        IllegalStateException e = new IllegalStateException("状态异常");
        log.warn("unExpected exception: {}", e.getLocalizedMessage(), e);
        log.info("exception", e);
    }
}
