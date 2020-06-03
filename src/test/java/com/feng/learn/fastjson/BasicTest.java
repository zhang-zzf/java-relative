package com.feng.learn.fastjson;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

/**
 * @author zhanfeng.zhang
 * @date 2020/05/19
 */
public class BasicTest {

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


}
