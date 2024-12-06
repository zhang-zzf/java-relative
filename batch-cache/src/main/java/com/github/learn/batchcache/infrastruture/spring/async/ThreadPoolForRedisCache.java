package com.github.learn.batchcache.infrastruture.spring.async;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 用于@Async异步任务.
 * <p>专用于处理**任务</p>
 * <p>使用前，必须更改此类的名字和THREAD_POOL_NAME</p>
 * <p>可以根据需求自定义线程池中的所有参数</p>
 */
@Configuration
public class ThreadPoolForRedisCache {

    public static final String THREAD_POOL_NAME = "redisCacheTaskExecutor";

    @Bean(THREAD_POOL_NAME)
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 获取cpu核数
        int cpuNum = Runtime.getRuntime().availableProcessors();

        // 以下参数可以按需调整
        // 必须给线程设置名称，方便日后排错
        taskExecutor.setThreadNamePrefix("spring-@Async-" + THREAD_POOL_NAME + "-");
        // 以下参数都是默认值
        // 核心线程。主要用于处理 IO
        taskExecutor.setCorePoolSize(cpuNum * 8);
        // 最大线程
        taskExecutor.setMaxPoolSize(cpuNum * 16);
        // BQ队列大小
        taskExecutor.setQueueCapacity(16);
        taskExecutor.setKeepAliveSeconds(60);
        // 拒绝策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 守护进程
        taskExecutor.setDaemon(false);

        return taskExecutor;
    }
}
