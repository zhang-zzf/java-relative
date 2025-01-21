package com.github.zzf.learn.utils;

import static java.time.format.DateTimeFormatter.ofPattern;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.SneakyThrows;

public class LogUtils {

    public static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.setDefaultPropertyInclusion(Include.NON_NULL);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // 日期序列化
        String pattern = "yyyy-MM-dd HH:mm:ss";
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(ofPattern(pattern)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(ofPattern("HH:mm:ss")));
        // 日期反序列化
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(ofPattern(pattern)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(ofPattern("HH:mm:ss")));
        mapper.registerModule(javaTimeModule);
    }

    @SneakyThrows
    public static String json(Object o) {
        return mapper.writeValueAsString(o);
    }

}
