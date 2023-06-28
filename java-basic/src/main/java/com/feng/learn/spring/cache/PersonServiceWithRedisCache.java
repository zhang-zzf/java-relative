package com.feng.learn.spring.cache;

import static com.feng.learn.spring.config.cache.SpringCacheConfig.REDIS_CACHE_MANAGER;
import static com.feng.learn.spring.config.cache.SpringCacheConfig.REDIS_CACHE_NAME_1;
import static com.feng.learn.spring.config.cache.SpringCacheConfig.REDIS_CACHE_NAME_2;

import java.util.function.Function;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/16
 */
@Service
@CacheConfig(cacheManager = REDIS_CACHE_MANAGER)
public class PersonServiceWithRedisCache {

  final Function justForTest;

  public PersonServiceWithRedisCache(Function justForTest) {
    this.justForTest = justForTest;
  }

  @Cacheable(key = "'/p/' + #root.args[0]",
      cacheNames = {REDIS_CACHE_NAME_1, REDIS_CACHE_NAME_2})
  public Person getById(Long id) {

    justForTest.apply(null);
    return new Person().setId(id).setName("zhanfeng.zhang")
        .setHome(new Person.Address().setCity("Shanghai"))
        .setWork(new Person.Address().setStreet("Zhenbei road"));
  }

  @CacheEvict(key = "'/p/' + #root.args[0]",
      cacheNames = {REDIS_CACHE_NAME_1, REDIS_CACHE_NAME_2})
  public void deleteById(Long id) {

  }

  @Data
  @FieldDefaults(level = AccessLevel.PRIVATE)
  @Accessors(chain = true)
  public static class Person {

    Long id;
    String name;

    Address home;
    Address work;

    @Data
    @Accessors(chain = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Address {

      String province;
      String city;
      String street;
    }
  }

}
