package com.github.zzf.actuator.rpc.http.provider.i18n_timezone;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-10-12
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/i18n/date")
public class DateController {

    @GetMapping("/")
    public String now(@RequestParam(required = false) String tz) {
        TimeZone reqZone = TimeZone.getDefault();
        if (tz != null) {
            reqZone = TimeZone.getTimeZone(tz);
        }
        TimeZone defaultZone = TimeZone.getDefault();
        log.info("new Date -> defaultZone: {}, requestZone: {}", defaultZone, reqZone);
        try {
            Date now = new Date();
            log.info("now in default zone -> {}, unix: {}", now, now.getTime());
            TimeZone.setDefault(reqZone);
            log.info("now in req zone -> {}, unix: {}", now, now.getTime());
            // 按默认时区格式化时间
            return now.toString();
        } finally {
            // 改为默认时区
            TimeZone.setDefault(defaultZone);
        }
    }

    @GetMapping("/calc")
    public Map<String, String> nowMap(@RequestParam(required = false) List<String> tz) {
        Date now = new Date();
        TimeZone defaultZone = TimeZone.getDefault();
        try {
            Map<String, String> ret = new LinkedHashMap<>();
            for (String zone : tz) {
                TimeZone timeZone = TimeZone.getTimeZone(zone);
                TimeZone.setDefault(timeZone);
                ret.put(timeZone.getID(), now.toString());
            }
            TimeZone.setDefault(defaultZone);
            ret.put(defaultZone.getID(), now.toString());
            return ret;
        } finally {
            // 改为默认时区
            TimeZone.setDefault(defaultZone);
        }
    }
}
