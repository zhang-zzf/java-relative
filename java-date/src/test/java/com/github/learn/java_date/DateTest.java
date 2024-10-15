package com.github.learn.java_date;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.learn.java_date.jackson.DateTimeBean;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-10-14
 */
@Slf4j
public class DateTest {

    /**
     * Date 101
     * <pre>
     *     类Date表示一个特定的时间瞬间，精度为毫秒。既然表示的是瞬间/时刻，那它必然和时区是无关的
     *     确切的说：Date对象里存的是自格林威治时间（GMT）1970年1月1日0点至Date所表示时刻所经过的毫秒数，是个数值
     * </pre>
     */
    @Test
    void givenDate_when101_then() {
        // now 内部仅包含 unix timestamp
        // timestamp 是时区无关的
        Date now = new Date();
        //
        // now.toString() 会根据当前环境的默认时区输出与时区关联的当地时间字符串
        // example: Tue Oct 15 17:57:52 CST 2024
        then(now.toString()).isNotNull();
        //
        // unix 时间戳
        long timestamp = now.getTime();
        then(timestamp).isGreaterThan(0L);
        //
        // Date -> String
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        // 2024-10-15T18:01:03.981+08:00
        String dateString = df.format(now);
        then(dateString).isNotNull();
    }

    /**
     * <pre>
     *     以下2个时区都代表东8区（CST）中国标准时间
     *
     *      - `Asia/Shanghai` 兼容夏令时
     *      - `GMT+08:00` 固定偏移量。设定时区偏移量时，是不能使用 UTC 的，只能使用 GMT！另外，使用非法的时区 ID 时，会将时区设定为零时区。
     * </pre>
     */
    @Test
    void givenTimeZone_when_then() {
        // 默认时区
        TimeZone tz = TimeZone.getDefault();
        // sun.util.calendar.ZoneInfo[id="Asia/Shanghai",offset=28800000,dstSavings=0,useDaylight=false,transitions=31,lastRule=null]
        then(tz).isNotNull();
        then(TimeZone.getTimeZone("UTC").getID()).isEqualTo("UTC");
        // 如何获取时区 ID
        // UTC, Universal, W-SU, WET, Zulu, EST, HST, MST, ACT, AET, AGT, ART, AST, BET, BST, CAT, CNT, CST, CTT, EAT, ECT, IET, IST, JST, MIT, NET, NST, PLT, PNT, PRT, PST, SST, VST
        // 洲/城市 -> Asia/Shanghai, America/New_York
        // 没有 Asia/Beijing id
        log.info("tz -> {}", (Object) TimeZone.getAvailableIDs());
        // 根据偏移量获取可用时区
        String[] availableIDsForGMT8 = TimeZone.getAvailableIDs(8 * 60 * 60 * 1000);
        Arrays.stream(availableIDsForGMT8).forEach(System.out::println);
        then(availableIDsForGMT8).contains("Asia/Shanghai");
        //
        // GMT+08:00 通用时区表示法，固定偏移量
        // 注意 GMT+08:00 不能使用 UTC+08:00 代替
        // 东8区
        then(TimeZone.getTimeZone("GMT+08:00").getOffset(0)).isEqualTo(28800000L);
        then(TimeZone.getTimeZone("GMT+0800").getOffset(0)).isEqualTo(28800000L);
        then(TimeZone.getTimeZone("GMT+08").getOffset(0)).isEqualTo(28800000L);
        // TimeZone 不识别 UTC+08:00 这种写法。返回默认时区 `GMT`
        then(TimeZone.getTimeZone("UTC+08:00").getID()).isEqualTo("GMT");
        then(TimeZone.getTimeZone("UTC+08:00").getOffset(0)).isEqualTo(0L);
        then(TimeZone.getTimeZone("Asia/Shanghai").getOffset(0)).isEqualTo(28800000L);
        // 西5区
        then(TimeZone.getTimeZone("GMT-05:00").getOffset(0)).isEqualTo(-18000000L);
        then(TimeZone.getTimeZone("America/New_York").getOffset(0)).isEqualTo(-18000000L);
    }

    /**
     * Asia/Shanghai 与 GMT+08 是有区别的
     * <pre>
     *     Asia/Shanghai 会兼容夏令时，在夏令时期间使用 GMT+09:00 时区
     *     GMT+08 是UTC固定偏移量
     * </pre>
     */

    @SneakyThrows
    @Test
    void givenTimeZone_whenDST_then() {
        // 在 Asia/Shanghai CST 时区，代表 1988-08-13 13:13:13
        Date date = new Date(587448793000L);
        // UTC 时区
        SimpleDateFormat dfOfUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dfOfUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        then(dfOfUTC.format(date)).isEqualTo("1988-08-13 04:13:13");
        // CST 时区，在夏令时期间使用 GMT+09 时区
        SimpleDateFormat dfOfCST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dfOfCST.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        then(dfOfCST.format(date)).isEqualTo("1988-08-13 13:13:13");
        // GMT+08 时区
        SimpleDateFormat dfOfGMT8 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dfOfGMT8.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        then(dfOfGMT8.format(date)).isEqualTo("1988-08-13 12:13:13");
        //
        String dateStr = "1988-08-13 13:13:13";
        // 在 CST 时区转成时间戳为    587448793000L
        // 在 GMT+08 时区转成时间戳为 587452393000L 比 CST 多 3600000 （1h）
        then(dfOfCST.parse(dateStr).getTime()).isEqualTo(587448793000L);
        then(dfOfGMT8.parse(dateStr).getTime()).isEqualTo(587452393000L);
    }

    @SneakyThrows
    @Test
    void givenDataTimePattern_when_then() {
        //
        Date utcZero = new Date(0);
        // General time zone
        String gtzp = "yyyy-MM-dd'T'HH:mm:ss.SSSz";
        // Date -> General time zone String
        SimpleDateFormat gtzdf = new SimpleDateFormat(gtzp);
        gtzdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String gtz = "1970-01-01T08:00:00.000CST";
        then(gtzdf.format(utcZero)).isEqualTo(gtz);
        // String -> Date
        SimpleDateFormat gtzdp = new SimpleDateFormat(gtzp);
        // dfz 优先使用 cstStr 字符串中的时区，忽略下面一行设置的的时区
        gtzdp.setTimeZone(TimeZone.getTimeZone("UTC"));
        then(gtzdp.parse(gtz).getTime()).isEqualTo(0L);
        //
        // 使用 RFC 822 time zone 可以解析 General time zone 的字符串
        String rfc822p = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        then(new SimpleDateFormat(rfc822p).parse(gtz).getTime()).isEqualTo(0L);
        //
        // RFC 822 time zone
        SimpleDateFormat rfc822df = new SimpleDateFormat(rfc822p);
        rfc822df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        // Date -> rfc822 String
        String rfc822 = rfc822df.format(utcZero);
        then(rfc822).isEqualTo("1970-01-01T08:00:00.000+0800");
        //
        then(new SimpleDateFormat(rfc822p).parse(rfc822).getTime()).isEqualTo(0L);
        // 使用 General time zone 可以解析 RFC 822 time zone 的字符串
        then(new SimpleDateFormat(gtzp).parse(rfc822).getTime()).isEqualTo(0L);
    }

    @SneakyThrows
    @Test
    void givenDataTimePattern_when_then() {
        //
        Date utcZero = new Date(0);
        // General time zone
        String gtzp = "yyyy-MM-dd'T'HH:mm:ss.SSSz";
        // Date -> General time zone String
        SimpleDateFormat gtzdf = new SimpleDateFormat(gtzp);
        gtzdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String gtz = "1970-01-01T08:00:00.000CST";
        then(gtzdf.format(utcZero)).isEqualTo(gtz);
        // String -> Date
        SimpleDateFormat gtzdp = new SimpleDateFormat(gtzp);
        // dfz 优先使用 cstStr 字符串中的时区，忽略下面一行设置的的时区
        gtzdp.setTimeZone(TimeZone.getTimeZone("UTC"));
        then(gtzdp.parse(gtz).getTime()).isEqualTo(0L);
        //
        // 使用 RFC 822 time zone 可以解析 General time zone 的字符串
        String rfc822p = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        then(new SimpleDateFormat(rfc822p).parse(gtz).getTime()).isEqualTo(0L);
        //
        // RFC 822 time zone
        SimpleDateFormat rfc822df = new SimpleDateFormat(rfc822p);
        rfc822df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        // Date -> rfc822 String
        String rfc822 = rfc822df.format(utcZero);
        then(rfc822).isEqualTo("1970-01-01T08:00:00.000+0800");
        //
        then(new SimpleDateFormat(rfc822p).parse(rfc822).getTime()).isEqualTo(0L);
        // 使用 General time zone 可以解析 RFC 822 time zone 的字符串
        then(new SimpleDateFormat(gtzp).parse(rfc822).getTime()).isEqualTo(0L);
    }


    @Test
    void givenDate_whenToZoneString_then() {
        Date utcZero = new Date(0);
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SimpleDateFormat defaultZoneDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        then(defaultZoneDf.format(utcZero)).isEqualTo("1970-01-01T08:00:00.000+08:00");
        SimpleDateFormat nyDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        nyDf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        then(nyDf.format(utcZero)).isEqualTo("1969-12-31T19:00:00.000-05:00");
    }

    /**
     * Date <-> long
     * <pre>
     *     UNIX时间戳（UNIX Time Stamp）为世界协调时间（Coordinated Universal Time，即UTC）1970年01月01日00时00分00秒到现在的总秒数，
     *     与时区无关。
     * </pre>
     */
    @Test
    void givenDate_whenConvertToLong_then() {
        long currentTimeMillis = System.currentTimeMillis();
        // long -> Date
        Date now = new Date(currentTimeMillis);
        // Date <- long
        long unixTimestamp = now.getTime();
        then(unixTimestamp).isEqualTo(currentTimeMillis);
    }

    /**
     * Date <-> long
     * <pre>
     *    1. It’s important to note that Jackson will serialize the Date to a timestamp format by default (number of milliseconds since January 1st, 1970, UTC).
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenDate_whenConvertToLongUseJackson_then() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date _1hUtc = df.parse("1970-01-01 01:00:00");
        DateTimeBean b = new DateTimeBean().setCreatedAt(_1hUtc)// 默认序列化成 unix time 数字 3600000
            ;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDefaultPropertyInclusion(Include.NON_NULL);
        String jsonStr = mapper.writeValueAsString(b);
        then(jsonStr).isEqualTo("{\"createdAt\":3600000}");
    }

    /**
     * Date <-> String
     * <pre>
     *     结论：针对 C/S 模式的服务
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 394e19a (feat: add more Date test)
     *     1. Client 与 Server 在同一个时区(utc8)，无需做额外处理。前后端都默认采取一个默认时区（uct8）来操作时间。
     *     2. Client 与 Server 在不同的时区，Client 分布在 utc0 / utc3 / utc8 时区，Server 部署在 utc0 区
     *        1. Server 端处理 Client 的 String 时间时，需要使用 Client 所在的时区来 parse String 字符串
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenDate_whenString_then() {
        // tz
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        //
        // String -> Date
        // 业务时间 -> 前后端约定隐含表示的是 utc8 时区的时间
        // String bizTime = "1970-01-01 08:00:00+08:00";
        String bizTime = "1970-01-01 08:00:00";
        // df 默认使用 TimeZone.getDefault() 时区
        // df.setTimeZone(TimeZone.getDefault());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date _zero_in_utc = df.parse(bizTime); // Thu Jan 01 08:00:00 CST 1970
        then(_zero_in_utc.getTime()).isEqualTo(0L);
        //
        // Date -> String
        // 隐含表示使用 默认时区 utc8 格式化字符串
        String formatDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(_zero_in_utc);
        then(formatDateStr).isEqualTo(bizTime);
    }

    /**
     * Date <-> String
     * <pre>
     *     结论：针对 C/S 模式的服务
<<<<<<< HEAD
=======
>>>>>>> 1b98ae3 (feat: add Date TimeZone)
=======
>>>>>>> 394e19a (feat: add more Date test)
     *     1. Client 与 Server 在同一个时区(utc8)，无需做额外处理
     *     2. Client 与 Server 在不同的时区，Client 分布在 utc0 / utc3 / utc8 时区，Server 部署在 utc0 区
     *        1. Server 端处理 Client 的 String 时间时，需要使用 Client 所在的时区来 parse String 字符串
     * </pre>
     *
     * <pre>
     *    "1970-01-01 00:00:00 CST" utc8时间
     *    1. 按 utc8 解析成 timestamp -28800000L
     *          1. 按东8区格式化 "1970-01-01 00:00:00 CST"
     *          1. 按 utc0 区格式化 "1969-12-31 16:00:00 UTC"
     *    1. 按 utc 解析成 timestamp 0L
     *          1. 按东8区格式化 "1970-01-01 08:00:00 CST"
     *          1. 按 utc0 区格式化 ""1970-01-01 00:00:00 UTC"
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenDate_whenConvertToString_then() {
        String bizTime = "1970-01-01 00:00:00";
        TimeZone utc8 = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone tz = TimeZone.getDefault();
        log.info("cur TZ -> {}", tz.getID());
        if (!tz.getID().equalsIgnoreCase("Asia/Shanghai")) {
            TimeZone.setDefault(utc8);
            tz = TimeZone.getDefault();
            log.info("UTC+8 TZ -> {}", tz.getID());
        }
        // 以 UTC+8 解析时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getDefault());
        Date _zero = df.parse(bizTime); // Thu Jan 01 00:00:00 CST 1970
        then(_zero.getTime()).isEqualTo(-28800000L); // 3600000*8 东8区
        // 以 UTC 时间来解析
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        df.setTimeZone(TimeZone.getDefault());
        Date _zero_utc = df.parse(bizTime);
        then(_zero_utc.getTime()).isEqualTo(0L);
        // UTC 解析的时间在东8区表示
        TimeZone.setDefault(utc8);
        df.setTimeZone(TimeZone.getDefault());
        then(df.format(_zero_utc)).isEqualTo("1970-01-01 08:00:00");
    }

    /**
     * Date <-> String with TimeZone
     * <pre>
     * //z  --> 2020-10-21T15:39:29.666CST
     * //Z-->2020-10-21T15:41:04.295+0800
     * //X-->2020-10-21T15:41:51.585+08
     * //XX-->2020-10-21T15:42:08.098+0800
     * //XXX-->2020-10-21T15:42:24.654+08:00
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenDate_whenConvertToStringWithZone_then() {
        TimeZone utc3 = TimeZone.getTimeZone("Asia/Riyadh");
        TimeZone.setDefault(utc3);
        Date _zero_utc = new Date(0); // utc0 1970-01-01 00:00:00
        // 按 utc8 格式化时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String dateStr = df.format(_zero_utc);
        then(dateStr).isEqualTo("1970-01-01T08:00:00.000+08:00");
        then(TimeZone.getDefault().getID()).isEqualTo("Asia/Riyadh");
        // 注意：下面的 DateFormat 没有设置时区，使用默认的 utc3 时区，但是可以正常解析 +08:00 时区的时间。
        // 人话说：SimpleDateFormat 会使用日期字符串中的时区来解析字符串
        then(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(dateStr).getTime()).isEqualTo(0L);
        //
        // TimeZone 优先级依次降低
        // 1. 字符串中的时区
        // 1. SimpleDateFormat.setTimeZone
        // 1. 默认时区
        SimpleDateFormat dfutc0 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dfutc0.setTimeZone(TimeZone.getTimeZone("UTC"));
        then(dfutc0.getTimeZone().getID()).isEqualTo("UTC");
        then(dfutc0.parse(dateStr).getTime()).isEqualTo(0L);
    }

    /**
     * Date <-> String with TimeZone
     * <pre>
     *     DateFormat 时区优先级
     *     // TimeZone 优先级依次降低
     *     // 1. 字符串中的时区
     *     // 1. DateFormat.TimeZone
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenString_whenConvertToDate_then() {
        // 设置 utc3 为默认时区
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Riyadh"));
        //
        then(TimeZone.getDefault().getID()).isEqualTo("Asia/Riyadh");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        // df 设置 utc 时区
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        then(df.getTimeZone().getID()).isEqualTo("UTC");
        Date parseByStrZone = df.parse("1970-01-01T08:00:00.000+08:00");
        // 结果：按字符串中的时区解析字符串
        then(parseByStrZone.getTime()).isEqualTo(0L); // utc 时间为 0 点
        //
        // pattern 中没有时区，会忽略 DateStr 中的时区
        then(TimeZone.getDefault().getID()).isEqualTo("Asia/Riyadh");
        SimpleDateFormat dfutc0 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        then(dfutc0.getTimeZone().getID()).isEqualTo("Asia/Riyadh");
        // 重置 DateFormat 时区
        dfutc0.setTimeZone(TimeZone.getTimeZone("UTC"));
        then(dfutc0.getTimeZone().getID()).isEqualTo("UTC");
        then(dfutc0.parse("1970-01-01T08:00:00.000+08:00").getTime()).isEqualTo(28800000L); // utc 时间为 8 点
        //
        //
        then(TimeZone.getDefault().getID()).isEqualTo("Asia/Riyadh");
        SimpleDateFormat dfUseDefaultZone = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        then(dfUseDefaultZone.getTimeZone().getID()).isEqualTo("Asia/Riyadh");
        Date parseByDefaultZone = dfUseDefaultZone.parse("1970-01-01T08:00:00.000+08:00");
        then(parseByDefaultZone.getTime()).isEqualTo(18000000L); // utc 时间为 5 点
        //
    }

    /**
     * String -> Date
     * <pre>
     *     结论： 解析 pattern < 时间格式 都可以被正常解析
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenDate_whenParseString_then() {
        TimeZone utc8 = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(utc8);
        Date _zero_utc = new Date(0); // utc8 1970-01-01 08:00:00
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String dateStr = df.format(_zero_utc);
        then(dateStr).isEqualTo("1970-01-01T08:00:00.000+08:00");
        then(catchThrowable(() -> {
            new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            new SimpleDateFormat("yyyy-MM-dd'T'HH").parse(dateStr);
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(dateStr);
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(dateStr);
        })).isNull();
    }

    /**
     * String -> Date
     * <pre>
     *     结论： 解析 pattern < 时间格式 都可以被正常解析
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenDate_whenParseString2_then() {
        // 设置东8区（北京时间）中国标准时间为默认时区
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        //
        String utcTime = "1970-01-01T00:00:00Z";
        // 忽略了 utcTimeStr 中的时区
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT+3"));// 设置时区
        // 以 utc3 解析时间，忽略了 时间字符串中的时区信息
        Date date = df.parse(utcTime);
        then(date.getTime()).isEqualTo(-10800000L);
        // 在东8区是 5 点
        then(date.toString()).isEqualTo("Thu Jan 01 05:00:00 CST 1970");
        // 截断成 `1970-01-01 00:00:00`
        then(new SimpleDateFormat("yyyy-MM-dd").parse(utcTime).getTime()).isEqualTo(-28800000L);
        // 截断成 `1970-01-01 00:00:00`
        then(new SimpleDateFormat("yyyy-MM-dd'T'HH").parse(utcTime).getTime()).isEqualTo(-28800000L);
        then(catchThrowable(() -> {
            new SimpleDateFormat("yyyy-MM-dd").parse(utcTime);
            new SimpleDateFormat("yyyy-MM-dd'T'HH").parse(utcTime);
            // 参考 SimpleDateFormat
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(utcTime);
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXX").parse(utcTime);
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(utcTime);
        })).isNull();
        // 无法解析
        then(catchThrowable(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(utcTime))).isNotNull();
        // 使用 DateStr 中的 `Z` 时区解析
        then(TimeZone.getDefault().getID()).isEqualTo("Asia/Shanghai");
        Date dateWithZone = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(utcTime);
        then(dateWithZone.getTime()).isEqualTo(0L);
        //
        //
        then(TimeZone.getDefault().getID()).isEqualTo("Asia/Shanghai");
        then(new SimpleDateFormat("yyyy-MM-dd").parse("1970-01-01T08Z").getTime())
            .isNotEqualTo(0L)
            .isEqualTo(-28800000L);
        then(new SimpleDateFormat("yyyy-MM-dd'T'HH").parse("1970-01-01T08Z").getTime())
            .isEqualTo(0L);
        then(new SimpleDateFormat("yyyy-MM-dd'T'HHXXX").parse("1970-01-01T08Z").getTime())
            .isEqualTo(28800000L);
    }

    /**
     * String 带时区 -> Date
     * <pre>
     *     结论:
     *       1. SimpleDateFormat 可以正常识别 DateStr 中的时区，并按 DateStr 中的时区解析时间（而不是按默认时区解析）
     *
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenStringWithZone_whenParseString2_then() {
        // 设置东8区（北京时间）中国标准时间为默认时区
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        //
        String utcTime = "1970-01-01T00:00:00Z";
        // 忽略时区, 按默认时区（东8区）解析当前时间
        then(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(utcTime).getTime()).isEqualTo(-28800000L);
        then(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(utcTime).getTime()).isEqualTo(-28800000L);
        // 带时区解析
        then(TimeZone.getDefault().getID()).isEqualTo("Asia/Shanghai");
        then(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(utcTime).getTime()).isEqualTo(0L);
        then(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXX").parse(utcTime).getTime()).isEqualTo(0L);
        then(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(utcTime).getTime()).isEqualTo(0L);
    }

    /**
     * String 带时区 -> Date
     * <pre>
     *     结论:
     *       1. SimpleDateFormat 可以正常识别 DateStr 中的时区，并按 DateStr 中的时区解析时间（而不是按默认时区解析）
     *       1. X / XX / XXX 只能解析各自的格式，不能使用 XXX 解析 X 格式的字符串
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenStringWithZone_whenParseString3_then() {
        // UTC 为默认时区
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        //
        String utc8Time = "1970-01-01T08:00:00+08";
        // 不带时区解析，使用 UTC 时区解析
        then(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(utc8Time).getTime()).isEqualTo(28800000L);
        // 带时区解析
        then(TimeZone.getDefault().getID()).isEqualTo("UTC");
        then(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(utc8Time).getTime()).isEqualTo(0L);
        // XX 无法解析 +08
        then(catchThrowable(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXX").parse(utc8Time))).isNotNull();
        // XXX 无法解析 +08
        then(catchThrowable(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(utc8Time))).isNotNull();
        // XX 解析 +0800
        then(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXX").parse("1970-01-01T08:00:00+0800").getTime()).isEqualTo(0L);
        // XXX 解析 +08:00
        then(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse("1970-01-01T08:00:00+08:00").getTime()).isEqualTo(0L);
    }

    /**
     * Date 与 Locale 国际化
     */
    @Test
    void givenDate_whenWithLocale_then() {
    }

    /**
     * Date 与 TimeZone 多时区国际化
     */
    @Test
    void givenDate_whenWithTimeZone_then() {
    }

    /**
     * String -> Date 时间截断
     */
    @SneakyThrows
    @Test
    void givenDateString_whenToDate_then() {
        // 1729036800000L 在 CST 时区表示 2024-10-16T08:00:00
        // 1729008000000L 在 CST 时区表示 2024-10-16T00:00:00
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        then(df.parse("2024-10-16T08:00:00").getTime()).isEqualTo(1729036800000L);
        //
        // 丢弃时间格式
        SimpleDateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");
        dfShort.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        then(dfShort.parse("2024-10-16T08:00:00").getTime()).isEqualTo(1729008000000L);
        then(dfShort.parse("2024-10-16T07:00:00").getTime()).isEqualTo(1729008000000L);
        then(dfShort.parse("2024-10-16T00:00:00").getTime()).isEqualTo(1729008000000L);
        then(dfShort.parse("2024-10-16").getTime()).isEqualTo(1729008000000L);
    }

    @Test
    void givenCalendar_when_then() {
        // 默认区 +8
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(0L);
        then(c.getTimeInMillis()).isEqualTo(0L);
        // utc
        Calendar cutc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cutc.setTimeInMillis(0L);
        then(cutc.getTimeInMillis()).isEqualTo(0L);
        //
        // 1729123200000 /  utc 2024-10-17 00:00 / utc3 2024-10-17 03:00
        // 设置 utc 为默认时区
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Date utc = new Date(1729123200000L);
        then(utc.getTime()).isEqualTo(1729123200000L);
        // utc
        SimpleDateFormat dfUtc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dfUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
        // utc+3
        SimpleDateFormat dfUtc3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dfUtc3.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        for (int i = -3; i < 21; i++) {
            Date date = new Date(1729123200000L + i * 60 * 60 * 1000);
            Date floorDate = floor(date, "GMT+3");
            log.info("\nbiz -> now: {}, floor: {}\nutc -> {}", dfUtc3.format(date), dfUtc3.format(floorDate), dfUtc.format(floorDate));
            then(dfUtc3.format(floorDate)).isEqualTo("2024-10-17T00:00:00.000+03:00");
        }
        // utc+8
        SimpleDateFormat dfUtc8 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dfUtc8.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        for (int i = -8; i < 16; i++) {
            Date date = new Date(1729123200000L + i * 60 * 60 * 1000);
            Date floorDate = floor(date, "Asia/Shanghai");
            log.info("\nbiz -> now: {}, floor: {}\nutc -> {}", dfUtc8.format(date), dfUtc8.format(floorDate), dfUtc.format(floorDate));
            then(dfUtc8.format(floorDate)).isEqualTo("2024-10-17T00:00:00.000+08:00");
        }
        // utc-4
        SimpleDateFormat dfUtc_4 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dfUtc_4.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        for (int i = 4; i < 28; i++) {
            Date date = new Date(1729123200000L + i * 60 * 60 * 1000);
            Date floorDate = floor(date, "America/New_York");
            log.info("\nbiz -> now: {}, floor: {}\nutc -> {}", dfUtc_4.format(date), dfUtc_4.format(floorDate), dfUtc.format(floorDate));
            then(dfUtc_4.format(floorDate)).isEqualTo("2024-10-17T00:00:00.000-04:00");
        }
    }

    private static Date floor(Date utc, String tz) {
        Calendar c3 = Calendar.getInstance(TimeZone.getTimeZone(tz));
        c3.setTime(utc);
        c3.set(Calendar.HOUR_OF_DAY, 0);
        c3.set(Calendar.MINUTE, 0);
        c3.set(Calendar.SECOND, 0);
        c3.set(Calendar.MILLISECOND, 0);
        return c3.getTime();
    }

}
