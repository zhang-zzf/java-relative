package com.feng.learn.spring.config.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.AbstractCacheResolver;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.support.NoOpCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhanfeng.zhang
 * @date 2020/6/7
 */
@Configuration
@EnableCaching
public class SpringCacheConfigWithSwitchControl {

  public static final String JVM_CACHE = "jvmCache";
  public static final String CACHE_RESOLVER = "switchAbilityCacheResolver";
  private static final String NO_CACHE = "noOpCache";
  private static final String CACHE_MANAGE = "concurrentMapCacheManager";

  /**
   * 定义 CacheManager
   *
   * @return CacheManager
   */
  @Bean(CACHE_MANAGE)
  public SimpleCacheManager simpleCacheManager() {
    // 需手动定义 Cache
    SimpleCacheManager manager = new SimpleCacheManager();

    NoOpCache noOpCache = new NoOpCache(NO_CACHE);
    ConcurrentMapCache concurrentMapCache = new ConcurrentMapCache(JVM_CACHE);

    manager.setCaches(Arrays.asList(noOpCache, concurrentMapCache));
    return manager;
  }

  @Bean(CACHE_RESOLVER)
  public SwitchAbilityCacheResolver switchAbilityCacheResolver(
      @Qualifier(CACHE_MANAGE) CacheManager cacheManager) {
    return new SwitchAbilityCacheResolver(cacheManager);
  }

  @Target({ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public static @interface CacheSwitch {

    String value() default "";
  }

  /**
   * 自定义可以动态开关 Cache 的 CacheResolver
   *
   * @author zhanfeng.zhang
   * @date 2020/6/7
   */
  public static class SwitchAbilityCacheResolver extends AbstractCacheResolver {

    private volatile boolean cacheSwitchOn = true;

    /**
     * Construct a new {@code SimpleCacheResolver} for the given {@link CacheManager}.
     *
     * @param cacheManager the CacheManager to use
     */
    public SwitchAbilityCacheResolver(CacheManager cacheManager) {
      super(cacheManager);
    }

    public void switchCacheOff() {
      cacheSwitchOn = false;
    }

    @Override
    protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
      String key = getCacheSwitchKey(context.getMethod());
      // 根据 key 判断是否关闭 cache
      if (cacheSwitchOn) {
        return context.getOperation().getCacheNames();
      }
      // 返回 NoOpCache
      return Collections.singletonList(NO_CACHE);
    }

    private String getCacheSwitchKey(Method method) {
      String switchKey;
      CacheSwitch cacheSwitch = method.getDeclaredAnnotation(CacheSwitch.class);
      if (cacheSwitch != null && !("".equals(cacheSwitch.value()))) {
        switchKey = cacheSwitch.value();
      } else {
        switchKey = method.getDeclaringClass().getName() + "#" + method.getName();
      }
      return switchKey;
    }
  }
}
