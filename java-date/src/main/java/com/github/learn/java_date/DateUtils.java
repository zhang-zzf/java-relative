package com.github.learn.java_date;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.time.LocalDateTime;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/05
 */
public class DateUtils {

    /**
     * String -> LocalDateTime
     *
     * @param value value
     * @param pattern pattern
     * @return data
     */
    public static LocalDateTime parse(String value, String pattern) {
        return LocalDateTime.parse(value, ofPattern(pattern));
    }

}
