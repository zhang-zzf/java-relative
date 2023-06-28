package com.feng.learn.spring.cache;

import static com.feng.learn.spring.config.cache.SpringCacheConfigWithSwitchControl.CACHE_RESOLVER;
import static com.feng.learn.spring.config.cache.SpringCacheConfigWithSwitchControl.JVM_CACHE;

import com.feng.learn.spring.config.cache.SpringCacheConfigWithSwitchControl;
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
@CacheConfig(cacheResolver = CACHE_RESOLVER)
public class PersonServiceWithSwitchAbilityCacheResolver {

  final Function justForTest;

  public PersonServiceWithSwitchAbilityCacheResolver(Function justForTest) {
    this.justForTest = justForTest;
  }

  @SpringCacheConfigWithSwitchControl.CacheSwitch("zhanfeng.zhang")
  @Cacheable(key = "'/p/' + #root.args[0]", cacheNames = {JVM_CACHE})
  public Person getById(Long id) {
    justForTest.apply(null);
    return new Person().setId(id).setName("zhanfeng.zhang")
        .setHome(new Person.Address().setCity("Shanghai"))
        .setWork(new Person.Address().setStreet("Zhenbei road"));
  }

  @CacheEvict(key = "'/p/' + #root.args[0]", cacheNames = {JVM_CACHE})
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
