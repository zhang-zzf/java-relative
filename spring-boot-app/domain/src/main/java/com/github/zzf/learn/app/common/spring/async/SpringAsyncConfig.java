package com.github.zzf.learn.app.common.spring.async;

import com.github.zzf.learn.app.common.spring.async.task_decorator.ContextPropagateDecorator;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@EnableAsync
@EnableScheduling
@Configuration
@Slf4j
public class SpringAsyncConfig {

    public static final String ASYNC_THREAD = "taskExecutor";
    public static final String SCHEDULER_THREAD = "taskScheduler";

    /**
     * Spring JVM 级别调度器
     */
    @Bean(name = SCHEDULER_THREAD)
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setThreadFactory(new NamedThreadFactory(SCHEDULER_THREAD));
        return threadPoolTaskScheduler;
    }

    /**
     * 用于@Async异步任务.
     * <p>直接丢弃处理不过来的任务</p>
     * <p>用于异步处理的线程池默认是单线程-无限BQ。这里修改线程池的核心线程数为[1, cpu*2]</p>
     * <p>@Async 可以指定使用哪个线程池执行异步处理，未指定时使用'taskExecutor'线程池</p>
     */
    @Bean(name = ASYNC_THREAD)
    public ThreadPoolTaskExecutor taskExecutor() {
        int cpuNum = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(cpuNum * 2);
        taskExecutor.setQueueCapacity(1024);
        taskExecutor.setKeepAliveSeconds(300);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        taskExecutor.setThreadFactory(new NamedThreadFactory(ASYNC_THREAD));
        taskExecutor.setTaskDecorator(new ContextPropagateDecorator(r -> r));
        return taskExecutor;
    }

    @Getter
    public static final class NamedThreadFactory implements ThreadFactory {
        private final String name;

        public NamedThreadFactory(String name) {
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, name);
        }

        public String name() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }
            var that = (NamedThreadFactory) obj;
            return Objects.equals(this.name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return "NamedThreadFactory[" +
                "name=" + name + ']';
        }

    }

}
