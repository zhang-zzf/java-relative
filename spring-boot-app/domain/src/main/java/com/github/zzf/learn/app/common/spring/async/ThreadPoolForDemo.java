package com.github.zzf.learn.app.common.spring.async;

import com.github.zzf.learn.app.common.spring.async.SpringAsyncConfig.NamedThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolForDemo {

    public static final String ASYNC_THREAD = "spring-@Async-demo";

    @Bean(name = ASYNC_THREAD)
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        int cpuNum = Runtime.getRuntime().availableProcessors();// 获取cpu核数
        // 以下参数可以按需调整 以下参数都是默认值
        taskExecutor.setThreadFactory(new NamedThreadFactory(ASYNC_THREAD));// 必须给线程设置名称，方便日后排错
        taskExecutor.setCorePoolSize(1);// 核心线程
        taskExecutor.setMaxPoolSize(cpuNum);// 最大线程
        taskExecutor.setQueueCapacity(16);// BQ队列大小
        taskExecutor.setKeepAliveSeconds(60);// 默认60s
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());// 拒绝策略
        taskExecutor.setDaemon(false);// 守护进程
        // taskExecutor.setTaskDecorator(new ContextPropagateDecorator(r -> r));// 上下文传递
        return taskExecutor;
    }

}
