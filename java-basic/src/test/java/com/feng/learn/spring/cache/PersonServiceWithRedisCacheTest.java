package com.feng.learn.spring.cache;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import com.feng.learn.spring.cache.PersonServiceWithRedisCache.Person;
import com.feng.learn.spring.cache.PersonServiceWithRedisCacheTest.ContextMock;
import com.feng.learn.spring.config.cache.SpringCacheConfig;
import com.feng.learn.spring.config.redis.EmbeddedServer;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/16
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    PersonServiceWithRedisCache.class,
    SpringCacheConfig.class,
    EmbeddedServer.class,
    ContextMock.class,
})
@DirtiesContext
@Disabled
@Ignore
public class PersonServiceWithRedisCacheTest {

  @Autowired
  PersonServiceWithRedisCache personService;
  @Autowired
  @Qualifier("justForTest")
  Function justFotTest;

  @Test
  public void givenCacheEnable_whenUsePersonService_then() {
    // when
    // first time no cache
    Person noCache = personService.getById(1L);
    // second time use cache. so there is still one invoke to the mock
    Person cache = personService.getById(1L);
    BDDMockito.then(justFotTest).should(times(1)).apply(any());
    then(cache).isEqualTo(noCache);

    // evict cache
    personService.deleteById(1L);
    personService.getById(1L);
    BDDMockito.then(justFotTest).should(times(2)).apply(any());
  }

  @Test
  public void given2CacheWithDifferentTtlTime_then() throws InterruptedException {
    // when
    // first time no cache
    Person noCache = personService.getById(1L);
    // 等待 REDIS_CACHE_NAME_1 缓存过期
    TimeUnit.SECONDS.sleep(SpringCacheConfig.REDIS_CACHE_NAME_1_TTL + 1);
    // second time use cache. so there is still one invoke to the mock
    // 从 REDIS_CACHE_NAME_2 缓存读取数据
    Person cache = personService.getById(1L);
    BDDMockito.then(justFotTest).should(times(1)).apply(any());
    then(cache).isEqualTo(noCache);
  }


  @Configuration
  static class ContextMock {

    @Bean
    public Function justForTest() {
      return Mockito.mock(Function.class);
    }
  }
}