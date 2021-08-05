package com.github.learn.java_date;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/05
 */
class DateUtilsTest {

    /**
     * 格式必须一一对应
     * <p>2021-03-26 14:37:29.0</p>
     * <p>yyyy-MM-dd HH:mm:ss.S</p>
     */
    @Test
    void givenString_whenConvertToLocalDateTime_then() {
        String value = "2021-03-26 14:37:29.0";
        final LocalDateTime localDateTime = DateUtils.parse(value, "yyyy-MM-dd HH:mm:ss.S");
        then(localDateTime).isNotNull();
    }

}