package com.feng.learn.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.*;

/**
 * @author zhanfeng.zhang
 * @date 2020/05/19
 */
public class BasicTest {

    @Test
    void givenBoolean_whenConvertToInt_thenSuccess() {
        // boolean to int
        //language=JSON
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
        //language=JSON
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
    public static class Holder {
        Long numStr;
        Integer flag;
        Boolean aInt;
    }

}
