package com.github.zzf.dd.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.github.zzf.dd.utils.LogUtils.json;


@Slf4j
public class MetricsUtil {


    public static void count(String name, long val, String... attrs) {
    }

    public static void countOnce(String name, String... attrs) {
    }

    public static void countWithTrace(String name, long val, String... attrs) {
    }

    public static void gaugeWithTrace(String name, long val, String... attrs) {

    }

    public static void gauge(String name, long val, String... attrs) {
    }


    /**
     * 适用 大部分请求 < 40ms
     */
    public static void fastTimer(String name, long val, String... attrs) {
    }

    /**
     * 适用 大部分请求 < 40ms
     */
    public static void fastTimerWithTrace(String name, long val, String... attrs) {
    }

    /**
     * 适用 大部分请求 < 300ms
     */
    public static void midTimer(String name, long val, String... attrs) {
    }

    /**
     * 适用 大部分请求 < 300ms
     */
    public static void midTimerWithTrace(String name, long val, String... attrs) {
    }

    /**
     * 适用 大部分请求 < 1200ms
     */
    public static void slowTimer(String name, long val, String... attrs) {
    }

    /**
     * 适用 大部分请求 < 1200ms
     */
    public static void slowTimerWithTrace(String name, long val, String... attrs) {
    }

    /**
     * 适用大部分请求 < 10000ms
     */
    public static void timer(String name, long val, String... attrs) {
    }

    public static void timerWithTrace(String name, long val, String... attrs) {
    }

    public static void doTimer(String name, long val, List<Double> bucketList, String... attrs) {
    }

    public static void logEvent(String type, String name, Object data, String... attrs) {
        logEvent(type, name, json(data), attrs);
        if (data instanceof Throwable) {

        }
    }

    public static void logEvent(String type, String name, String data, String... attrs) {

    }

}
