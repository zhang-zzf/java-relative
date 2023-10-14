package com.github.learn.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.learn.jackson.deserialization.AliasBean;
import com.github.learn.jackson.serialization.*;
import lombok.SneakyThrows;
import lombok.var;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(MockitoExtension.class)
class JacksonCaseTest {

    static ObjectMapper mapper;

    @BeforeAll
    public static void beforeClass() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @SneakyThrows
    @Test
    void givenJSON_whenToList_then() {
        InputStream json = ClassLoader.getSystemResourceAsStream("json-list.json");
        // Java Object 需要使用 TypeReference, 基础类型 String / Integer 不需要
        // List<String> list = mapper.readValue(json, List.class);
        List<Bean2> list = mapper.readValue(json, new TypeReference<List<Bean2>>() {});
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
     *     JsonFormat 格式化 Date
     * </pre>
     */
    @SneakyThrows
    @Test
    void givenJsonFormat_when_then() {
        Date createdAt = new Date();
        LocalDateTime updatedAt = LocalDateTime.now();
        var ab = new ABean().setCreatedAt(createdAt).setUpdatedAt(updatedAt);
        var jsonStr = mapper.writeValueAsString(ab);
        ABean aBean = mapper.readValue(jsonStr, ABean.class);
        then(aBean).returns(createdAt, ABean::getCreatedAt)
                .returns(updatedAt, ABean::getUpdatedAt)
        ;
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
