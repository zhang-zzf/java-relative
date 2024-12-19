package com.github.learn.jackson;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.learn.jackson.deserialization.AliasBean;
import com.github.learn.jackson.serialization.ExtendableBean;
import com.github.learn.jackson.serialization.SexEnum;
import com.github.learn.jackson.serialization.SexEnumObj;
import com.github.learn.jackson.serialization.SexEnumWithJsonValue;
import com.github.learn.jackson.serialization.UnExtendableBean;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.var;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JacksonCaseTest {

    static ObjectMapper mapper;

    @BeforeAll
    public static void beforeClass() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * jackson 序列化上带上 类型信息
     */
    @SneakyThrows
    @Test
    void givenAtClass_when_then() {
        TypeBean typeBean = new TypeBean();
        String json = mapper.writeValueAsString(typeBean);
        then(json).isNotNull();
        //
        //
        ObjectMapper dateStrMapper = new ObjectMapper();
        // java8 java.time
        dateStrMapper.registerModule(new JavaTimeModule());
        // 同时对 java.time.* / java.util.Date 生效
        dateStrMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
        String dateStringJson = dateStrMapper.writeValueAsString(typeBean);
        then(dateStringJson).isNotNull();
        //
        //
        ObjectMapper dateStrAndTypeMapper = new ObjectMapper();
        // java8 java.time
        dateStrAndTypeMapper.registerModule(new JavaTimeModule());
        // 同时对 java.time.* / java.util.Date 生效
        dateStrAndTypeMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
        dateStrAndTypeMapper.activateDefaultTyping(
            LaissezFaireSubTypeValidator.instance,
            DefaultTyping.JAVA_LANG_OBJECT,
            As.PROPERTY
            );
        String typedJson = dateStrAndTypeMapper.writeValueAsString(typeBean);
        then(typedJson).isNotNull();
        then(dateStrAndTypeMapper.readValue(typedJson, Object.class))
            .isInstanceOf(TypeBean.class)
            .isEqualTo(typeBean);
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
            // 默认序列化成 unix time 数字 1697350229079
            .setCreatedAt(createdAt)
            // 默认序列化成 [2023,10,15,14,10,29,797180000]
            .setUpdatedAt(updatedAt)
            // 默认序列化成 [2023,10,15]
            .setLocalDate(updatedAt.toLocalDate())
            // 默认序列化成 [14,10,29,797180000]
            .setLocalTime(updatedAt.toLocalTime());
        String jsonStr = mapper.writeValueAsString(b);
        then(jsonStr).isEqualTo(
            "{\"createdAt\":1697350229079,\"updatedAt\":[2023,10,15,14,10,29,797180000],\"localDate\":[2023,10,15],\"localTime\":[14,10,29,797180000]}");
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
        customMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // java8 java.time
        customMapper.registerModule(new JavaTimeModule());
        String bJson = customMapper.writeValueAsString(b);
        then(bJson).isEqualTo(
            "{\"createdAt\":\"2023-10-15 14:10:29\",\"updatedAt\":\"2023-10-15T14:10:29.79718\",\"localDate\":\"2023-10-15\",\"localTime\":\"14:10:29.79718\"}");
        // todo
    }

    @SneakyThrows
    @Test
    void givenDate_whenSerializationAndDeserializationUseISO_then() {
        Date createdAt = new Date(1697350229079L);
        LocalDateTime updatedAt = LocalDateTime.parse("2023-10-15T14:10:29.79718000");
        DateTimeBean b = new DateTimeBean()
            // 默认序列化成 unix time 数字 1697350229079
            .setCreatedAt(createdAt)
            // 默认序列化成 [2023,10,15,14,10,29,797180000]
            .setUpdatedAt(updatedAt)
            // 默认序列化成 [2023,10,15]
            .setLocalDate(updatedAt.toLocalDate())
            // 默认序列化成 [14,10,29,797180000]
            .setLocalTime(updatedAt.toLocalTime());
        String jsonStr = mapper.writeValueAsString(b);
        String defaultJsonStr = "{\"createdAt\":1697350229079,\"updatedAt\":[2023,10,15,14,10,29,797180000],\"localDate\":[2023,10,15],\"localTime\":[14,10,29,797180000]}";
        then(jsonStr).isEqualTo(defaultJsonStr);
        ObjectMapper customMapper = new ObjectMapper();
        // java8 java.time
        customMapper.registerModule(new JavaTimeModule());
        // 同时对 java.time.* / java.util.Date 生效
        customMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
        String bJson = customMapper.writeValueAsString(b);
        String isoDateStr = "{\"createdAt\":\"2023-10-15T14:10:29.079+08:00\","
            + "\"updatedAt\":\"2023-10-15T14:10:29.79718\","
            + "\"localDate\":\"2023-10-15\","
            + "\"localTime\":\"14:10:29.79718\"}";
        then(bJson).isEqualTo(isoDateStr);
    }

    /**
     * <pre>
     * 反序列化时忽略多余字段
     * JSON 的字段比 Java Object 的字段多，抛出 UnrecognizedPropertyException 异常
     *      * 解决方案1：类上添加 `@JsonIgnoreProperties(ignoreUnknown = true)`
     *      * 解决方案2：全局 mapper 配置 customMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
     * </prep>
     */
    @SneakyThrows
    @Test
    void givenJSONHasMorePropertyThanJavaObject_whenDeserialization_thenSuccess() {
        String jsonStr = "  {\n" +
            "    \"order\": \"third\",\n" +
            "    \"name\": \"whatever\",\n" +
            "    \"unrecognizedProperty\": true" +
            "  }\n";
        // 默认抛出 UnrecognizedPropertyException
        Throwable t = catchThrowable(() -> mapper.readValue(jsonStr, Bean2.class));
        then(t).isInstanceOf(UnrecognizedPropertyException.class);
        // 解决方案1
        // 类上添加注解 @JsonIgnoreProperties(ignoreUnknown = true) // Deserialization 遇到本类中没有的字段不报错
        var b4 = mapper.readValue(jsonStr, Bean4.class);
        then(b4).isNotNull();
        // 解决方案2
        // 全局配置
        ObjectMapper customMapper = new ObjectMapper();
        customMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        var b2 = customMapper.readValue(jsonStr, Bean2.class);
        then(b2).isNotNull();
    }

    /**
     * <pre>
     * Serialization ignore null Field
     * 序列化时忽略 null 字段
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenNullField_whenSerialization_then() {
        var b3 = new Bean3().setId(1L);
        // 默认情况下, 输出 null 字段
        String b3Json = mapper.writeValueAsString(b3);
        then(b3Json).contains("null").isEqualTo("{\"id\":1,\"order\":null,\"name\":null}");
        // JSON 中去 null 方案1
        // 类上添加 @JsonInclude(value = NON_NULL, content = NON_NULL)
        var b4 = new Bean4().setId(1L);
        String b4Json = mapper.writeValueAsString(b4);
        then(b4Json).doesNotContain("null");
        // JSON 中去 null 方案2
        // ObjectMapper 全局配置
        ObjectMapper customMapper = new ObjectMapper();
        customMapper.setSerializationInclusion(NON_NULL);
        String b3JsonWithoutNull = customMapper.writeValueAsString(b3);
        then(b3JsonWithoutNull).doesNotContain("null");
    }

    /**
     * <pre>
     * ObjectMapper 配置
     *      Java Object 有 primitive 字段， JSON 无对应的字段，不报错。
     *      Java Object 有 primitive 字段， JSON 对应的字段为 null ，报错。
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenJavaObjectHasPrimitiveField_whenDeserialization_then() {
        ObjectMapper mapper = new ObjectMapper();
        // 默认 false，Java Object 的 primitive fields 被初始化成默认值
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        String jsonStr = "  {\n" +
            "    \"order\": \"third\",\n" +
            "    \"name\": \"whatever\"\n" +
            // "    \"id\": null" +
            "  }\n";
        // 反常识的点：若 JSON 中缺失字段，不报错。只有 JSON 中有字段且为 null 时报错。
        var b = mapper.readValue(jsonStr, Bean2.class);
        then(b).isNotNull();
    }

    /**
     * JSON to Map
     */
    @SneakyThrows
    @Test
    void givenJSON_whenToMap_then() {
        InputStream json = ClassLoader.getSystemResourceAsStream("json-demo.json");
        var map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });
        then(map).isNotEmpty();
    }

    /**
     * JSON to List
     */
    @SneakyThrows
    @Test
    void givenJSON_whenToList_then() {
        InputStream json = ClassLoader.getSystemResourceAsStream("json-list.json");
        // Java Object 需要使用 TypeReference, 基础类型 String / Integer 不需要
        // List<String> list = mapper.readValue(json, List.class);
        List<Bean2> list = mapper.readValue(json, new TypeReference<List<Bean2>>() {
        });
        then(list).isNotEmpty();
    }

    @SneakyThrows
    @Test
    void givenJSON_whenReadTree_then() {
        InputStream json = ClassLoader.getSystemResourceAsStream("json-demo.json");
        JsonNode node = mapper.readTree(json);
        then(node.get("age").asInt()).isEqualTo(18);
        then(node.get("address").get("province").asText()).isEqualTo("AnHui");
    }

    /**
     * <pre>
     *     JsonUnwrapped
     *     把对象展平到当前对象
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenJsonUnwrapped_when_then() {
        var u = new UnwrappedUser().setId(1L).setName(new UnwrappedUser.Name("zhang", "zzf"));
        String jsonStr = mapper.writeValueAsString(u);
        then(jsonStr).isEqualTo("{\"id\":1,\"firstName\":\"zhang\",\"lastName\":\"zzf\"}");
        UnwrappedUser uu = mapper.readValue(jsonStr, UnwrappedUser.class);
        then(uu).returns("zhang", o -> o.getName().getFirst());
    }

    /**
     * <pre>
     * JsonProperty
     * json字段名字 <-> POJO 字段名字映射
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenJsonProperty_when_then() {
        var aBean = new ABean().setId(1).setName("zhang.zzf");
        var jsonStr = mapper.writeValueAsString(aBean);
        then(jsonStr).isEqualTo("{\"id\":1,\"userName\":\"zhang.zzf\"}");
        ABean d = mapper.readValue(jsonStr, ABean.class);
        then(d).returns(1, ABean::getId)
            .returns("zhang.zzf", ABean::getName);
    }

    /**
     * 反序列化别名
     */
    @SneakyThrows
    @Test
    void givenJsonAlias_when_then() {
        @Language("JSON") var jsonStr = "{\"fName\": \"zhang\", \"lastName\": \"zzf\"}";
        var aB = mapper.readValue(jsonStr, AliasBean.class);
        then(aB).returns("zhang", AliasBean::getFirstName);
    }

    /**
     * Enum 序列化
     */
    @Test
    @SneakyThrows
    void givenJsonValue_when_then() {
        var obj = new SexEnumObj()
            .setSexEnum(SexEnum.MALE)
            .setSexEnumWithJsonValue(SexEnumWithJsonValue.FEMALE);
        String json = mapper.writeValueAsString(obj);
        then(json).isEqualTo("{\"sexEnum\":\"MALE\",\"sexEnumWithJsonValue\":\"女\"}");
        // deserialization
        var sexEnumObj = mapper.readValue(json, SexEnumObj.class);
        then(sexEnumObj).returns(SexEnum.MALE, SexEnumObj::getSexEnum)
            .returns(SexEnumWithJsonValue.FEMALE, SexEnumObj::getSexEnumWithJsonValue);
    }

    /**
     * <pre>
     *   序列化
     *   把 Map 中的字段展平 unwrapping
     *   对照 {@link JacksonCaseTest#givenNoJsonAnyGetter_when_then()}
     *  {@link com.fasterxml.jackson.annotation.JsonAnyGetter} 与 {@link com.fasterxml.jackson.annotation.JsonAnySetter} 一起使用
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenJsonAnyGetter_when_then() {
        var b = new ExtendableBean("myBean").add("attr1", "val1");
        String jsonString = mapper.writeValueAsString(b);
        then(jsonString).isEqualTo("{\"name\":\"myBean\",\"attr1\":\"val1\"}");
        var eB = mapper.readValue(jsonString, ExtendableBean.class);
        then(eB).returns("myBean", ExtendableBean::getName)
            .returns("val1", d -> d.getProperties().get("attr1"));
    }

    /**
     * <pre>
     *   序列化
     *   把 Map 中的字段展平 unwrapping
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenNoJsonAnyGetter_when_then() {
        var b = new UnExtendableBean("myBean").add("attr1", "val1");
        String jsonString = mapper.writeValueAsString(b);
        then(jsonString).isEqualTo("{\"name\":\"myBean\",\"properties\":{\"attr1\":\"val1\"}}");
    }


}
