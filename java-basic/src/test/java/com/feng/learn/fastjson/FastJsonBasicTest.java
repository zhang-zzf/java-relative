package com.feng.learn.fastjson;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.from;
import static org.assertj.core.api.BDDAssertions.then;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import java.time.LocalDateTime;
import lombok.Data;
import org.junit.jupiter.api.Test;

/**
 * @author zhanfeng.zhang
 * @date 2020/05/19
 */
public class FastJsonBasicTest {

    /**
     * 测试数组
     */
    @Test
    void givenArrayJson_whenDeserialize_then() {
        final String chinaDefault = "{\n  "
            + "  \"lat\": [3, 54],\n"
            + "  \"lon\": [73, 136]\n"
            + "}";
        CoordinateLimit limit = JSONObject.parseObject(chinaDefault, CoordinateLimit.class);
        then(limit.getLat()).hasSize(2);
    }

    /**
     * 测试 LocalDateTime
     */
    @Test
    void givenLocalDateTime_thenJson_then() {
        JSONObject j = new JSONObject();
        LocalDateTime now = LocalDateTime.now();
        j.put("time", now);
        String s = j.toJSONString();
        JSONObject jsonObject = JSON.parseObject(s);
        LocalDateTime time = jsonObject.getObject("time", LocalDateTime.class);
        then(now).isEqualTo(time);
    }

    /**
     * 测试静态内部类的 json 序列化/反序列化
     */
    @Test
    void givenStaticInnerClass_whenJson_then() {
        Person p = new Person(1L);
        p.setName("zzf.zhang");
        Person.Address a = new Person.Address();
        a.setHome("home");
        a.setStreet("street");
        p.setAddress(a);
        String json = JSON.toJSONString(p);
        Person person = JSON.parseObject(json, Person.class);
        then(person).returns(1L, from(Person::getId))
            .extracting(Person::getAddress).isNotNull();
    }

    @Test
    void givenNullField_whenConvertToJsonString_thenDoNotContainNull() {
        Holder h = new Holder();
        h.setAInt(true);
        String jsonString = JSON.toJSONString(h);
        then(jsonString).doesNotContain("null");
    }

    @Test
    void givenJsonObject() {
        String json = "{\"flag\":true}";
        JSONObject jsonObject = JSON.parseObject(json, JSONObject.class);
        then(jsonObject.getBooleanValue("flag")).isTrue();
        then(JSON.toJSONString(jsonObject)).isEqualTo(json);
    }

    @Test
    void givenNull_whenToJsonString_thenSuccess() {
        String str = null;
        String jsonStr = JSON.toJSONString(str);
        then(jsonStr).isEqualTo("null");
    }

    @Test
    void givenInheritModel_whenConvertJson_thenSuccess() {
        Parent sub = new Sub();
        sub.setAge(1);
        ((Sub) sub).setName("zhanfeng");
        String json = JSON.toJSONString(sub);

        Parent parent = JSON.parseObject(json, Parent.class);
        Sub sub1 = JSON.parseObject(json, Sub.class);
    }

    @Test
    void givenBoolean_whenConvertToInt_thenSuccess() {
        // boolean to int
        // language=JSON
        String json = "{\"flag\": true}";
        Holder holder = JSON.parseObject(json, Holder.class);
        then(holder.getFlag()).isEqualTo(1);
        json = "{\"flag\": false}";
        holder = JSON.parseObject(json, Holder.class);
        then(holder.getFlag()).isEqualTo(0);
        // int to boolean
        json = "{\"aInt\": 1}";
        holder = JSON.parseObject(json, Holder.class);
        then(holder.getAInt()).isTrue();
        json = "{\"aInt\": 0}";
        holder = JSON.parseObject(json, Holder.class);
        then(holder.getAInt()).isFalse();
    }

    @Test
    void givenString_whenConvertToLong_thenSuccess() {
        // language=JSON
        String json = "{\"numStr\": \"55\"}";
        Holder holder = JSON.parseObject(json, Holder.class);
        then(holder.getNumStr()).isEqualTo(55L);
    }

    @Test
    void givenNull_whenParseObject_thenReturnNull() {
        String json = null;
        then(JSON.parseObject(json)).isNull();
    }

    @Test
    void givenEmptyString_whenParseObject_thenReturnNull() {
        String json = "";
        then(JSON.parseObject(json)).isNull();
    }

    @Test
    void givenArray_whenParseObject_thenException() {
        String json = "[]";
        Throwable t = catchThrowable(() -> {
            JSON.parseObject(json);
        });
        then(t).isInstanceOf(JSONException.class);
    }

    @Test
    void givenJson_whenGetNotExistField_thenReturnNull() {
        String json = "{}";
        String field = "aNotExistField";
        JSONObject jsonObject = JSON.parseObject(json);
        then(jsonObject.get(field)).isNull();
        then(jsonObject.getInteger(field)).isNull();
    }

    @Test
    void givenJson_whenGetNotExistFieldOfPrimitive_thenReturnDefaultValue() {
        String json = "{}";
        String field = "aNotExistField";
        JSONObject jsonObject = JSON.parseObject(json);
        then(jsonObject.getIntValue(field)).isEqualTo(0);
        then(jsonObject.getLongValue(field)).isEqualTo(0L);
        then(jsonObject.getBooleanValue(field)).isFalse();
    }

    @Data
    public static class CoordinateLimit {

        double lat[];
        double lon[];
    }

    @Data
    public static class Holder {

        Long numStr;
        Integer flag;
        Boolean aInt;
    }


    @Data
    public static class Parent {

        private int age;
    }

    @Data
    public static class Sub extends Parent {

        private String name;
    }
}
