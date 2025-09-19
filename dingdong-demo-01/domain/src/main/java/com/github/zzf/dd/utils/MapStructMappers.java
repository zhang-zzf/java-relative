package com.github.zzf.dd.utils;

import com.ddmc.fdc.base.info.domain.annotation.Nullable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Named;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class MapStructMappers {

    @Named("integerListToCommaStr")
    public static String integerListToCommaStr(List<Integer> integerList) {
        if (integerList == null) {
            return null;
        }
        return integerList.stream().map(String::valueOf).collect(joining(","));
    }

    @Named("commaStrToIntegerList")
    public static List<Integer> commaStrToIntegerList(String str) {
        if (StringUtils.isEmpty(str)) {/* null 保存到 db 为 '', 所以 '' 反序列化成 null */
            return null;
        }
        return Arrays.stream(str.split(","))
                .map(MapStructMappers::parseInt)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private static Integer parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {// ignore
            return null;
        }
    }


    @SneakyThrows
    @Named("stringDefaultToEmpty")
    public String stringDefaultToEmpty(String o) {
        return ofNullable(o).orElse("");
    }

    @SneakyThrows
    @Named("integerDefaultZero")
    public Integer integerDefaultZero(Integer o) {
        return ofNullable(o).orElse(0);
    }

    @SneakyThrows
    @Named("toJSONStr")
    @Nullable
    public String toJSONStr(Object o) {
        if (o == null) return null;
        return MAPPER.writeValueAsString(o);
    }

    @SneakyThrows
    @Named("toStringList")
    @Nullable
    public List<String> toStringList(String json) {
        if (json == null) return null;
        return parseList(json, String.class);
    }

    @Named("emptyListToNull")
    @Nullable
    public <T> List<T> emptyListToNull(List<T> origin) {
        if (origin == null || origin.isEmpty()) {
            return null;
        }
        return origin;
    }

    @Named("nullToEmptyList")
    @Nullable
    public <T> List<T> nullToEmptyList(List<T> origin) {
        return origin == null ? emptyList() : origin;
    }

    public static <T> List<T> parseList(String json, Class<T> type) {
        return parseCollection(json, List.class, type);
    }

    public static <T> Set<T> parseSet(String json, Class<T> type) {
        return parseCollection(json, Set.class, type);
    }


    private static final ObjectMapper MAPPER = jacksonMapper();

    private static ObjectMapper jacksonMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 反序列化时候遇到不匹配的属性并不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 序列化时候遇到空对象不抛出异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 反序列化的时候如果是无效子类型,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        // json 中不包含 null 字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 不使用默认的dateTime进行序列化,
        // objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        // 使用JSR310提供的序列化类,里面包含了大量的JDK8时间序列化类
        objectMapper.registerModule(new JavaTimeModule());
        // ISO8601
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
        // 需要配置模型上的 @JsonTypeInfo(use = Id.CLASS) 使用
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, JsonTypeInfo.As.PROPERTY);
        return objectMapper;
    }

    @SneakyThrows
    private static <V, C extends Collection<?>, T> V parseCollection(
        String json,
        Class<C> collectionType,
        Class<T> elementType) {
        TypeFactory typeFactory = MAPPER.getTypeFactory();
        CollectionType javaType = typeFactory.constructCollectionType(collectionType, elementType);
        return MAPPER.readValue(json, javaType);
    }

}
