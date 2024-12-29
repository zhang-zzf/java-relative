package com.github.zzf.dd.repo.redis;


import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-15
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("mini234b")
@Slf4j
public class RedisSerializerTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void givenJackson_whenSerialize_then() {
        NoClassTypeInfo noClassTypeInfo = new NoClassTypeInfo().setStr("NoClassTypeInfo");
        ClassTypeInfo classTypeInfo = new ClassTypeInfo().setStr("ClassTypeInfo");
        redisTemplate.opsForValue().set("noClassTypeInfo", noClassTypeInfo);
        redisTemplate.opsForValue().set("classTypeInfo", classTypeInfo);
        // Could not read JSON: Could not resolve subtype of [simple type, class java.lang.Object]: missing type id property '@class' at [Source: (byte[])"{"str":"NoClassTypeInfo"}"; line: 1, column: 25]
        Throwable throwable = catchThrowable(() -> redisTemplate.opsForValue().get("noClassTypeInfo"));
        then(throwable).isNotNull();
        // 无法转换成 NoClassTypeInfo
        Object o2 = redisTemplate.opsForValue().get("classTypeInfo");
        then(o2).isNotNull().isInstanceOf(ClassTypeInfo.class);
    }

    @Data
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    public static class ClassTypeInfo {
        String str;
    }

    @Data
    public static class NoClassTypeInfo {
        String str;
    }

}