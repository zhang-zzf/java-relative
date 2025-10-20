package com.github.zzf.dd.common;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public abstract class ConfigService {

    public abstract String queryStr(@NotNull String key, String defaultVal);

    /**
     * 获取 int 类型配置
     */
    public abstract int queryInt(@NotNull String key, int defaultVal);

    /**
     * 获取开关。 配置的值可以为 [0,100]。 0-关闭，100-开启，50-50% 开启
     */
    public boolean switchOn(@NotNull String key, boolean defaultVal) {
        return randomBetween0And100() < queryInt(key, defaultVal ? 100 : 0);
    }

    /**
     * @return [0, 99]
     */
    public static int randomBetween0And100() {
        return ThreadLocalRandom.current().nextInt(100);
    }

}
