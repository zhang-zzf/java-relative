package com.feng.learn.spring.redis;

import static com.feng.learn.spring.config.redis.StandaloneRedisConfig.REDIS_TEMPLATE;
import static org.assertj.core.api.Assertions.assertThat;

import com.alibaba.fastjson.JSON;
import com.feng.learn.spring.config.redis.EmbeddedServer;
import com.feng.learn.spring.config.redis.StandaloneRedisConfig;
import javax.annotation.Resource;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/05
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    StandaloneRedisConfig.class,
    EmbeddedServer.class,
})
@DirtiesContext
public class StandaloneRedisTest {

    final Person p = new Person().setId(1L).setName("zhanfeng.zhang").setAge(31);
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisTemplate<String, String> stringStringRedisTemplate;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Resource(name = REDIS_TEMPLATE)
    ValueOperations<String, Object> valueOperations;

    @Test
    public void testFastJsonSerialize() {
        String key = "/p/" + p.getId();
        valueOperations.set(key, p);
        assertThat((Person) valueOperations.get(key)).isEqualTo(p);
    }

    @Test
    public void testJson() {
        String str = "zhanfeng.zhang";
        String strJson = JSON.toJSONString(str);
        String aString = JSON.parseObject(strJson, String.class);
        assertThat(aString).isEqualTo(str);

        boolean b = false;
        String bJson = JSON.toJSONString(b);
        Boolean aBoolean = JSON.parseObject(bJson, Boolean.class);
        assertThat(aBoolean).isEqualTo(b);

        Object o = null;
        String oJson = JSON.toJSONString(o);
        Object aObject = JSON.parseObject(oJson, Object.class);
        assertThat(aObject).isEqualTo(o);
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Accessors(chain = true)
    @NoArgsConstructor
    public static class Person {

        Long id;
        String name;
        int age;

        public Person(Long id) {
            this.id = id;
        }
    }


}
