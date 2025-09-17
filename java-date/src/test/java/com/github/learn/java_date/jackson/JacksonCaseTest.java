package com.github.learn.java_date.jackson;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.var;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(MockitoExtension.class)
class JacksonCaseTest {

    static ObjectMapper mapper;

    @BeforeAll
    public static void beforeClass() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDefaultPropertyInclusion(Include.NON_NULL);
    }

    /**
     * <pre>
     * Date / Instant / LocalDateTime / ZonedDateTime / OffsetDatetime <-> str
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenDate_whenJSONtoStr_then() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDefaultPropertyInclusion(Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 优先级比 WRITE_DATES_AS_TIMESTAMPS 优先级高
        // 设置时间格式后所有的时间都会按照这个时间格式进行序列化
        // 设置时间格式后把 Date 类型按 JVM 时区序列化。不设置时按 UTC 时区序列化。仅针对 Date 有区别
        // mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"));
        String jsonDateFormat = mapper.writeValueAsString(new DateBean001(0L));
        String dateFormatStr = "{" +
                "\"date\":\"1970-01-01T00:00:00.000+00:00\"," +
                "\"instant\":\"1970-01-01T00:00:00Z\"," +
                "\"localDateTime\":\"1970-01-01T08:00:00\"," +
                "\"zonedDateTime\":\"1970-01-01T08:00:00+08:00\"," +
                "\"offsetDateTime\":\"1970-01-01T08:00:00+08:00\"," +
                "\"localDate\":\"1970-01-01\"," +
                "\"localTime\":\"08:00:00\"}";
        then(jsonDateFormat).isNotNull().isEqualTo(dateFormatStr);
        then(mapper.readValue(jsonDateFormat, DateBean001.class)).isNotNull();
        //
        // long 类型可以反序列化成 时间对象
        String jsonDateBean001 = "{\"date\":0," +
                "\"instant\":0.0," +
                "\"localDateTime\":[1970,1,1,8,0]," +
                "\"zonedDateTime\":0.0," +
                "\"offsetDateTime\":0.0," +
                "\"localDate\":[1970,1,1]," +
                "\"localTime\":[8,0]}";
        then(mapper.readValue(jsonDateBean001, DateBean001.class)).isNotNull();
        //
        String dateFormatStr01 = "{" +
                "\"date\":\"1970-01-01T08:00:00+08:00\"," +
                "\"instant\":\"1970-01-01T00:00:00Z\"," +
                "\"localDateTime\":\"1970-01-01T08:00:00\"," +
                "\"zonedDateTime\":\"1970-01-01T08:00:00+08:00\"," +
                "\"offsetDateTime\":\"1970-01-01T08:00:00+08:00\"," +
                "\"localDate\":\"1970-01-01\"," +
                "\"localTime\":\"08:00:00\"}";
        then(mapper.readValue(dateFormatStr01, DateBean001.class)).isNotNull();

    }

    /**
     * <pre>
     * Date / Instant /  ZonedDateTime / OffsetDatetime <-> long
     * LocalDateTime / LocalDate / LocalTime 无法序列化成 long
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenDate_whenJSONtoLong_then() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDefaultPropertyInclusion(Include.NON_NULL);
        // 把时间序列化成 timestamp
        // 默认开启
        // mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //
        // 世界时间（可以转换成世界时间）序列化成 long 。Date / Instant / ZonedDateTime / OffsetDatetime
        // LocalDateTime / LocalDate / LocalTime 无法序列化成 long
        String json = mapper.writeValueAsString(new DateBean001(0L));
        String jsonDateBean001 = "{\"date\":0," +
                "\"instant\":0.0," +
                "\"localDateTime\":[1970,1,1,8,0]," +
                "\"zonedDateTime\":0.0," +
                "\"offsetDateTime\":0.0," +
                "\"localDate\":[1970,1,1]," +
                "\"localTime\":[8,0]}";
        then(json).isNotNull().isEqualTo(jsonDateBean001);
        then(mapper.readValue(json, DateBean001.class)).isNotNull();
        //
        // 默认支持 把 ISO str 反序列化成 时间对象
        String dateFormatStr = "{" +
                "\"date\":\"1970-01-01T08:00:00+08:00\"," +
                "\"instant\":\"1970-01-01T00:00:00Z\"," +
                "\"localDateTime\":\"1970-01-01T08:00:00\"," +
                "\"zonedDateTime\":\"1970-01-01T08:00:00+08:00\"," +
                "\"offsetDateTime\":\"1970-01-01T08:00:00+08:00\"," +
                "\"localDate\":\"1970-01-01\"," +
                "\"localTime\":\"08:00:00\"}";
        then(mapper.readValue(dateFormatStr, DateBean001.class)).isNotNull();
    }

    /**
     * <pre>
     *     java.util.Date / java.time.LocalDateTime 序列化及反序列化
     *     Date -> 默认序列化成 unix timestamp 1697350229079
     *     LocalDateTime -> 默认序列化成 [2023,10,15,14,10,29,797180000]
     *
     *     // 方案1 Java Object 属性上添加 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
     *     // 方案2
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenDate_whenSerializationAndDeserialization_then() {
        Date createdAt = new Date(1697350229079L);
        LocalDateTime updatedAt = LocalDateTime.parse("2023-10-15T14:10:29.79718000");
        DateTimeBean b = new DateTimeBean()
                .setCreatedAt(createdAt)// 默认序列化成 unix time 数字 1697350229079
                .setUpdatedAt(updatedAt)// 默认序列化成 [2023,10,15,14,10,29,797180000]
                .setZonedDateTime(updatedAt.atZone(ZoneId.of("Asia/Shanghai")))
                .setLocalDate(updatedAt.toLocalDate())// 默认序列化成 [2023,10,15]
                .setLocalTime(updatedAt.toLocalTime());// 默认序列化成 [14,10,29,797180000]
        String jsonStr = mapper.writeValueAsString(b);
        then(jsonStr).isEqualTo(
                "{\"createdAt\":1697350229079,\"updatedAt\":[2023,10,15,14,10,29,797180000],\"localDate\":[2023,10,15],\"localTime\":[14,10,29,797180000],\"zonedDateTime\":1697350229.797180000}");
        DateTimeBean dd = mapper.readValue(jsonStr, DateTimeBean.class);
        then(dd).returns(createdAt, DateTimeBean::getCreatedAt).returns(updatedAt, DateTimeBean::getUpdatedAt);
        //
        // 方案1 Java Object 属性上添加 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        var d2 = new DateTimeBean2().setCreatedAt(createdAt).setUpdatedAt(updatedAt)
                .setLocalDate(updatedAt.toLocalDate()).setLocalTime(updatedAt.toLocalTime());
        var d2Json = mapper.writeValueAsString(d2);
        then(d2Json).isEqualTo(
                "{\"createdAt\":\"2023-10-15 06:10:29.079\",\"updatedAt\":\"2023-10-15 14:10:29.797180\",\"localDate\":\"2023-10-15\",\"localTime\":\"14:10:29.797180\"}");
        DateTimeBean2 dd2 = mapper.readValue(d2Json, DateTimeBean2.class);
        then(dd2).returns(createdAt, DateTimeBean2::getCreatedAt).returns(updatedAt, DateTimeBean2::getUpdatedAt);
        // 方案2 设置 ObjectMapper
        ObjectMapper customMapper = new ObjectMapper();
        customMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
        // java8 java.time
        customMapper.registerModule(new JavaTimeModule());
        String bJson = customMapper.writeValueAsString(b);
        then(bJson).isEqualTo("{\"createdAt\":\"2023-10-15T14:10:29.079+08:00\","
                + "\"updatedAt\":\"2023-10-15T14:10:29.79718\","
                + "\"localDate\":\"2023-10-15\","
                + "\"localTime\":\"14:10:29.79718\","
                + "\"zonedDateTime\":\"2023-10-15T14:10:29.79718+08:00\"}");
    }


    /**
     * <pre>
     * long/ String -> Date
     * 1. Date 字段上无注解
     *      1. long 可以转 Date
     *      1. ISO 标准时间的字符串可以转 Date yyyy-MM-ddTHH:mm:ss.SSSXXX
     *          1. 若字符串中没有携带时区信息，按 Z 时区解析
     * 1. Date 字段上有 JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") 注解
     *      1. 字符串可以转 Date
     *          1. 未指定时区，使用UTC时区转换 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
     *          1. 指定时区使用指定的时区转换  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Shanghai")
     *      1. long 可以转 Date
     *
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenString_whenToDate_then() {
        // 设置默认时区
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        then(TimeZone.getDefault().getID()).isEqualTo("Asia/Shanghai");
        //
        then(mapper.readValue("{\"createdAt\":\"1970-01-01T08:00:00.000Z\"}", DateTimeBean.class).getCreatedAt()
                .getTime())
                .isEqualTo(28800000L)
        ;
        then(mapper.readValue("{\"createdAt\":\"1970-01-01T08:00:00.000\"}", DateTimeBean.class).getCreatedAt()
                .getTime())
                //.isEqualTo(0L) // 若按默认时区解析
                .isEqualTo(28800000L) // iso 字符串中没有时区，按 Z 时区解析
        ;
        then(mapper.readValue("{\"createdAt\":\"1970-01-01T08:00\"}", DateTimeBean.class).getCreatedAt().getTime())
                .isEqualTo(28800000L) // iso 字符串中没有时区，按 Z 时区解析
        ;
        then(mapper.readValue("{\"createdAt\":\"1970-01-01\"}", DateTimeBean.class).getCreatedAt().getTime())
                .isEqualTo(0L) /* iso 字符串中没有时区，按 Z 时区解析 */;
        // 无法解析
        then(catchThrowable(() -> mapper.readValue("{\"createdAt\":\"1970-01-01+03:00\"}", DateTimeBean.class)))
                .isNotNull();
        // 无法解析
        then(catchThrowable(() -> mapper.readValue("{\"createdAt\":\"2023-10-15T06\"}", DateTimeBean.class)))
                .isNotNull();
        //
        then(mapper.readValue("{\"createdAt\":\"1970-01-01T08:00:00.000+08\"}", DateTimeBean.class).getCreatedAt()
                .getTime())
                .isEqualTo(0L) // 按字符串中的时区解析
        ;
        then(mapper.readValue("{\"createdAt\":\"1970-01-01T03:00:00.000+0300\"}", DateTimeBean.class).getCreatedAt()
                .getTime())
                .isEqualTo(0L) // 按字符串中的时区解析
        ;
        then(mapper.readValue("{\"createdAt\":\"1970-01-01T03:00:00.000+03:00\"}", DateTimeBean.class).getCreatedAt()
                .getTime())
                .isEqualTo(0L) // 按字符串中的时区解析
        ;
        then(mapper.readValue("{\"createdAt\":\"1970-01-01T03:00:00+03:00\"}", DateTimeBean.class).getCreatedAt()
                .getTime())
                .isEqualTo(0L) // 按字符串中的时区解析
        ;
        then(
                mapper.readValue("{\"createdAt\":\"1970-01-01T03:00+03:00\"}", DateTimeBean.class).getCreatedAt().getTime())
                .isEqualTo(0L) // 按字符串中的时区解析
        ;
        // long -> Date
        then(mapper.readValue("{\"createdAt\": 1729127817000}", DateTimeBean.class).getCreatedAt()).isNotNull();
        // 字段上有注解也可是使用 long -> Date
        then(mapper.readValue("{\"createdAt\": 1729127817000}", DateTimeBean2.class).getCreatedAt()).isNotNull();
        // 未指定时区，使用UTC时区转换
        // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        then(mapper.readValue("{\"createdAt\": \"1970-01-01 08:00:00.000\"}", DateTimeBean2.class).getCreatedAt()
                .getTime())
                .isEqualTo(28800000L)
                .isNotNull();
        // 指定时区使用指定的时区转换
        // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Shanghai")
        then(mapper.readValue("{\"createdAt2\": \"1970-01-01 08:00:00.000\"}", DateTimeBean2.class).getCreatedAt2()
                .getTime())
                .isEqualTo(0L)
                .isNotNull();
        //
        // @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
        // 时间戳会完全忽略 JsonFormat
        // 28800000 = 1970-01-01T08:00:00
        then(mapper.readValue("{\"createdAt3\": 28800000}", DateTimeBean2.class).getCreatedAt3().getTime())
                .isEqualTo(28800000L)
                .isNotNull();
        //
        // 无法转换，必须和格式保持一致
        // 1970-01-01 -> 无法赋值到 yyyy-MM-dd HH:mm:ss.SSS
        then(
                catchThrowable(() -> mapper.readValue("{\"createdAt\": \"1970-01-01\"}", DateTimeBean2.class))).isNotNull();
        // 1970-01-01 08:00:00 可以赋值到 @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
        then(mapper.readValue("{\"createdAt3\": \"1970-01-01 08:00:00\"}", DateTimeBean2.class).getCreatedAt3()
                .getTime())
                .isEqualTo(-28800000L);
    }


    @Data
    public static class DateBean001 {
        Date date = new Date();
        Instant instant = Instant.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        public DateBean001() {
        }

        public DateBean001(long timestamp) {
            date = new Date(timestamp);
            instant = Instant.ofEpochMilli(timestamp);
            localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
            offsetDateTime = OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
            localDate = localDateTime.toLocalDate();
            localTime = localDateTime.toLocalTime();
        }
    }

}
