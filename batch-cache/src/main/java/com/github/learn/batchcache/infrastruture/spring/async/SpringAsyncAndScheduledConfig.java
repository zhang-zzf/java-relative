package com.github.learn.batchcache.infrastruture.spring.async;

import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 基于spring的异步及调度线程池通用配置
 */
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
@Aspect
@Configuration
@Slf4j
public class SpringAsyncAndScheduledConfig {

  public static final String ASYNC_THREAD = "taskExecutor";
  public static final String SCHEDULER_THREAD = "taskScheduler";

  /**
   * 用于@Scheduled定时调度.
   * <p>所有@Scheduled 调度任务公用线程池</p>
   */
  @Bean(name = SCHEDULER_THREAD)
  public ThreadPoolTaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setThreadNamePrefix("spring-@Scheduled-");
    return taskScheduler;
  }

  /**
   * 用于@Async异步任务.
   * <p>用于异步处理的线程池默认是单线程-无限BQ。这里修改线程池的核心线程数为cpu的数量</p>
   * <p>@Async 可以指定使用哪个线程池执行异步处理，未指定时使用'taskExecutor'线程池</p>
   */
  @Bean(name = ASYNC_THREAD)
  public ThreadPoolTaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
    taskExecutor.setThreadNamePrefix("spring-@Async-default-");
    return taskExecutor;
  }

  /**
   * 为提交给taskExecutor 的任务添加线程传递信息
   */
  @Around(value = "bean(" + ASYNC_THREAD + ") && args(callable)", argNames = "pjp,callable")
  public Object addInfoToSpringAsyncTask(ProceedingJoinPoint pjp, Callable callable)
      throws Throwable {
    String key = "Hello, World";
    log.debug("{} set: key => {}", Thread.currentThread(), key);
    Callable taskWithTrace = () -> {
      log.debug("{} get: key => {}", Thread.currentThread(), key);
      return callable.call();
    };
    return pjp.proceed(new Object[]{taskWithTrace});
  }

  @Around(value = "bean(" + ASYNC_THREAD + ") && args(runnable)", argNames = "pjp,runnable")
  public Object addInfoToSpringTaskExecutor(ProceedingJoinPoint pjp, Runnable runnable)
      throws Throwable {
    String key = "Hello, World";
    log.debug("{} set: key => {}", Thread.currentThread(), key);
    Runnable taskWithTrace = () -> {
      log.debug("{} get: key => {}", Thread.currentThread(), key);
      runnable.run();
    };
    return pjp.proceed(new Object[]{taskWithTrace});
  }
}
