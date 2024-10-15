package com.github.learn.java_date;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.learn.java_date.jackson.DateTimeBean;
import java.text.SimpleDateFormat;
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
        //
        // GMT+08:00 通用时区表示法
        // 注意 GMT+08:00 不能使用 UTC+08:00 代替
        // 东8区
        then(TimeZone.getTimeZone("GMT+08:00").getOffset(0)).isEqualTo(28800000L);
        then(TimeZone.getTimeZone("Asia/Shanghai").getOffset(0)).isEqualTo(28800000L);
        // 西5区
        then(TimeZone.getTimeZone("GMT-05:00").getOffset(0)).isEqualTo(-18000000L);
        then(TimeZone.getTimeZone("America/New_York").getOffset(0)).isEqualTo(-18000000L);
    }


    @Test
    void givenDate_whenToZoneString_then() {
        Date utcZero = new Date(0);
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
        //
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
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));// 设置时区UTC
        Date date = df.parse(utcTime);
        then(date.getTime()).isEqualTo(0L);
        // 在东8区是 8 点
        then(date.toString()).isEqualTo("Thu Jan 01 08:00:00 CST 1970");
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
        // 带时区解析
        then(TimeZone.getDefault().getID()).isEqualTo("Asia/Shanghai");
        Date dateWithZone = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(utcTime);
        then(dateWithZone.getTime()).isEqualTo(0L);
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
        then(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse("1970-01-01T08:00:00+08:00").getTime()).isEqualTo(
            0L);
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

}
