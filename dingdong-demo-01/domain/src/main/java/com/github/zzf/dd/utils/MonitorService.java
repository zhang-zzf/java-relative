package com.github.zzf.dd.utils;

public interface MonitorService {

    void logEvent(String type, String name, Object data, String... attrs);

    void timer(String name, long val, String... attrs);
}
