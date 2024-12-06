package com.feng.learn.spring.redis;

import static com.feng.learn.spring.redis.ClusterRedisTest.ClusterRedisConfig.REDIS_TEMPLATE;
import static org.assertj.core.api.Assertions.assertThat;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import javax.annotation.Resource;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author zhanfeng.zhang
 * @date 2020/04/07
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    ClusterRedisTest.ClusterRedisConfig.class,
})
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

    @Configuration
    public static class ClusterRedisConfig {

        public static final String REDIS_FACTORY = "cluster";
        public static final String STRING_REDIS_TEMPLATE = REDIS_FACTORY + "StringRedisTemplate";
        public static final String REDIS_TEMPLATE = REDIS_FACTORY + "RedisTemplate";


        @Bean(REDIS_FACTORY)
        public RedisConnectionFactory redisConnectionFactory() {
            return new JedisConnectionFactory(new RedisClusterConfiguration()
                .clusterNode("macmini", 7700));
        }

        @Bean(STRING_REDIS_TEMPLATE)
        public StringRedisTemplate stringRedisTemplate(
            @Qualifier(REDIS_FACTORY) RedisConnectionFactory factory) {
            return new StringRedisTemplate(factory);
        }

        @Bean(REDIS_TEMPLATE)
        public RedisTemplate redisTemplate(@Qualifier(REDIS_FACTORY) RedisConnectionFactory factory) {
            RedisTemplate redisTemplate = new RedisTemplate();
            redisTemplate.setConnectionFactory(factory);
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setDefaultSerializer(new GenericFastJsonRedisSerializer());
            return redisTemplate;
        }

    }

}
