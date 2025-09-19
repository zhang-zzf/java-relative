package com.github.zzf.dd.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-09-18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigServiceSpringEnvImp extends ConfigService {

    final Environment environment;

    @Override
    public String queryStr(String key, String defaultVal) {
        return environment.getProperty(key, defaultVal);
    }

    @Override
    public int queryInt(String key, int defaultVal) {
        try {
            return environment.getProperty(key, Integer.class, defaultVal);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    @Override
    public boolean querySwitchOn(String key, boolean b) {
        try {
            return randomBetween0And100() <= queryInt(key, 0);
        } catch (Exception e) {
           return b;
        }
    }
    
}
