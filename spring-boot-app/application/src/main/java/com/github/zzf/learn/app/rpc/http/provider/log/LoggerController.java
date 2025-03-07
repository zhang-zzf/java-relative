package com.github.zzf.learn.app.rpc.http.provider.log;

import static java.util.Collections.emptyList;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-01-09
 */
@RestController
@RequestMapping("/loggers")
@Slf4j
public class LoggerController {

    @GetMapping("")
    public List<LoggerLevel> getLoggerLevels() {
        if (LoggerFactory.getILoggerFactory() instanceof LoggerContext loggerContext) {
            List<Logger> loggerList = loggerContext.getLoggerList();
            return loggerList.stream()
                .filter(logger -> logger.getLevel() != null)
                .map(logger -> new LoggerLevel(logger.getName(), logger.getLevel().levelStr))
                .toList()
                ;
        }
        return emptyList();
    }


    @GetMapping("/{loggerName}")
    public LoggerLevel getLoggerLevel(@PathVariable String loggerName) {
        if (LoggerFactory.getILoggerFactory() instanceof LoggerContext loggerContext) {
            Logger logger = loggerContext.getLogger(loggerName);
            return new LoggerLevel(loggerName, logger.getEffectiveLevel().levelStr);
        }
        return new LoggerLevel(loggerName, "unknown");
    }

    @PutMapping("/{loggerName}")
    public void setLoggerLevel(
        @PathVariable String loggerName,
        @RequestBody @Valid LoggerLevel level) { // 实测 @Controller 上无需 @Validated 也可以验证
        if (LoggerFactory.getILoggerFactory() instanceof LoggerContext loggerContext) {
            Logger logger = loggerContext.getLogger(loggerName);
            logger.setLevel(Level.toLevel(level.getLevel()));
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoggerLevel {
        String logger;
        @NotNull
        String level;
    }

}
