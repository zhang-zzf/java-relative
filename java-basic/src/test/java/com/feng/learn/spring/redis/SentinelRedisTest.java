package com.feng.learn.spring.redis;

import static com.feng.learn.spring.redis.SentinelRedisTest.SentinelRedisConfig.REDIS_TEMPLATE;
import static org.assertj.core.api.BDDAssertions.then;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Resource;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
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
    SentinelRedisTest.SentinelRedisConfig.class,
})
public class SentinelRedisTest {

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

    List<String> stringList = Stream.iterate(0, i11 -> i11 + 1).limit(1000).map(String::valueOf)
        .collect(Collectors.toList());

    stringList.forEach(str -> stringRedisTemplate.delete(str));

    stringList.forEach(str -> stringRedisTemplate.opsForValue().set(str, str));

    then(Stream.iterate(0, i1 -> i1 + 1).limit(1000).map(String::valueOf)
        .allMatch(str -> stringRedisTemplate.opsForValue().get(str).equals(str))).isTrue();
  }

  @Configuration
  public static class SentinelRedisConfig {

    public static final String REDIS_FACTORY = "sentinel";
    public static final String STRING_REDIS_TEMPLATE = REDIS_FACTORY + "StringRedisTemplate";
    public static final String REDIS_TEMPLATE = REDIS_FACTORY + "RedisTemplate";

    @Bean(REDIS_FACTORY)
    public RedisConnectionFactory redisConnectionFactory() {
      return new JedisConnectionFactory(new RedisSentinelConfiguration()
          .master("m_s")
          .sentinel("macmini", 27700));
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
