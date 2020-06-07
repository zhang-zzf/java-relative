package com.feng.learn.spring.cache;

import com.feng.learn.spring.cache.PersonServiceWithLocalRemoteComposeCache.Person;
import com.feng.learn.spring.cache.PersonServiceWithRedisCacheTest.ContextMock;
import com.feng.learn.spring.config.cache.SpringCacheConfig;
import com.feng.learn.spring.config.redis.EmbeddedServer;
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

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.feng.learn.spring.config.cache.SpringCacheConfig.LOCAL_REMOTE_CACHE_LOCAL_TTL;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/16
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    PersonServiceWithLocalRemoteComposeCache.class,
    SpringCacheConfig.class,
    EmbeddedServer.class,
    ContextMock.class,
})
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@Ignore
@Disabled
public class PersonServiceWithLocalRemoteComposeCacheTest {

    @Autowired
    PersonServiceWithLocalRemoteComposeCache personService;
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
    public void givenCacheEnableAndLocalCacheExpire_whenUsePersonService_thenGetFromRemoteCache()
        throws InterruptedException {
        // when
        // first time no localCache
        Person noCache = personService.getById(1L);
        // second time use local cache. so there is still one invoke to the mock
        Person localCache = personService.getById(1L);
        // Jvm 内由于有 local cache 的存在，所以2次返回结果是同一个对象
        then(localCache).isSameAs(noCache);
        BDDMockito.then(justFotTest).should(times(1)).apply(any());
        // 本地缓存过期
        TimeUnit.SECONDS.sleep(LOCAL_REMOTE_CACHE_LOCAL_TTL + 1);
        Person remoteCache = personService.getById(1L);
        then(remoteCache).isEqualTo(noCache).isNotSameAs(noCache);
        BDDMockito.then(justFotTest).should(times(1)).apply(any());
    }

    @Configuration
    static class ContextMock {

        @Bean
        public Function justForTest() {
            return Mockito.mock(Function.class);
        }
    }
}

