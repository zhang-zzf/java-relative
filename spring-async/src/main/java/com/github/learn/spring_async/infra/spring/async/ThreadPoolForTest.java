package com.github.learn.spring_async.infra.spring.async;

import com.github.learn.spring_async.infra.spring.async.SpringAsyncConfig.NamedThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author zhang.zzf@alibaba-inc.com
 * @date 2021/08/10
 */
@Configuration
public class ThreadPoolForTest {

    public static final String ASYNC_THREAD = "spring-@Async-for-test";

    @Bean(name = ASYNC_THREAD)
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 获取cpu核数
        int cpuNum = Runtime.getRuntime().availableProcessors();
        // 以下参数可以按需调整
        // 必须给线程设置名称，方便日后排错
        taskExecutor.setThreadFactory(new NamedThreadFactory(ASYNC_THREAD));
        // 以下参数都是默认值
        //核心线程
        taskExecutor.setCorePoolSize(1);
        // 最大线程
        taskExecutor.setMaxPoolSize(cpuNum);
        // BQ队列大小
        taskExecutor.setQueueCapacity(16);
        // 默认60s
        taskExecutor.setKeepAliveSeconds(60);
        // 拒绝策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 守护进程
        taskExecutor.setDaemon(false);
        return taskExecutor;
    }

}
