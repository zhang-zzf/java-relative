package com.github.zzf.learn.common;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Common {

    public static final BigDecimal _0 = new BigDecimal(0);

    public static final String _MOBILE_REGEXP = "(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}";
    public static final String MOBILE_REGEXP = "^" + _MOBILE_REGEXP + "$";
    public static final String SMS_CODE_REGEXP = "^\\d{4,8}$";
    public static final String YYYY_MM_REGEXP = "^\\d{4}-\\d{2}$";
    public static final Pattern MOBILE_REGEXP_PATTERN = Pattern.compile(MOBILE_REGEXP);

    public static final String X_WX_MP_OPEN_ID = "x-wx-mp-openid";
    public static final String X_WX_MP_APP_ID = "x-wx-mp-appid";

    public static String monthStr(@NotNull LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(yyyyMM);
    }

    /**
     * 返回 monthStr 月开始时间
     */
    public static LocalDateTime montStrToDate(@NotNull String monthStr) {
        String[] yyyyMM = monthStr.split("-");
        return LocalDate.of(Integer.parseInt(yyyyMM[0]), Integer.parseInt(yyyyMM[1]), 1).atStartOfDay();
    }

    public static final DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyy-MM");

}
