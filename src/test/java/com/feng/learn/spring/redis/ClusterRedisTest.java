package com.feng.learn.spring.redis;

import com.feng.learn.spring.config.redis.ClusterRedisConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static com.feng.learn.spring.config.redis.ClusterRedisConfig.REDIS_TEMPLATE;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author zhanfeng.zhang
 * @date 2020/04/07
 */
@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = {
//    ClusterRedisConfig.class,
//})
public class ClusterRedisTest {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate<String, String> stringStringRedisTemplate;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Resource(name = REDIS_TEMPLATE)
    ValueOperations<String, Object> valueOperations;

    // 需要依赖环境
    @Test
    @Ignore
    public void testCluster() {
        for (int i = 0; i < 100; i++) {
            String str = String.valueOf(i);
            stringRedisTemplate.opsForValue().set(str, str);
        }

        for (int i = 0; i < 100; i++) {
            String str = stringRedisTemplate.opsForValue().get(String.valueOf(i));
            assertThat(str).isEqualTo(String.valueOf(i));
        }
    }

}
