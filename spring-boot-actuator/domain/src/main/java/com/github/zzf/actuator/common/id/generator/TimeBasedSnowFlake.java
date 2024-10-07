package com.github.zzf.actuator.common.id.generator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeBasedSnowFlake {

    public static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * generate id use time
     * <p>不保证 id 不重复</p>
     * <p>${yyyyMMddHHmmss} * 100000 + new Random().nextInt(99999)</p>
     * <p>sample: 20210818 220256 06288</p>
     *
     * @return id
     */
    public static long generate() {
        final String time = YYYY_MM_DD_HH_MM_SS.format(LocalDateTime.now());
        return Long.parseLong(time) * 100000 + ThreadLocalRandom.current().nextInt(99999);
    }

}
