package com.github.zzf.learn.common.spring.async;


import com.github.zzf.learn.common.spring.async.SpringAsyncConfig.NamedThreadFactory;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * spring context thread pool monitor
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SpringAsyncThreadsMonitor {

    public static final String METRIC_NAME = SpringAsyncThreadsMonitor.class.getSimpleName();
    final List<ThreadPoolTaskExecutor> allExecutorInSpringContext;
    final List<ThreadPoolTaskScheduler> allSchedulerInSpringContext;

    /**
     * 开启应用内所有异步线程监控的监控
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void asyncTaskExecutorMonitor() {
        try {
            for (ThreadPoolTaskExecutor e : allExecutorInSpringContext) {
                // 线程数量监控
                final ThreadFactory tf = e.getThreadPoolExecutor().getThreadFactory();
                if (!(tf instanceof NamedThreadFactory)) {
                    continue;
                }
                final String threadPoolName = ((NamedThreadFactory) tf).getName();
                final BlockingQueue<Runnable> queue = e.getThreadPoolExecutor().getQueue();
                // todo metrics
                log.debug("SpringAsyncThreadsMonitor threadPoolTaskExecutor: {}, {}, {}, {}, {}, {}, {}",
                    threadPoolName,
                    e.getActiveCount(), e.getCorePoolSize(), e.getPoolSize(), e.getMaxPoolSize(),
                    queue.size(), queue.remainingCapacity() + queue.size()
                );
            }
            for (ThreadPoolTaskScheduler e : allSchedulerInSpringContext) {
                // 线程数量监控
                final ThreadFactory tf = e.getScheduledThreadPoolExecutor().getThreadFactory();
                if (!(tf instanceof NamedThreadFactory)) {
                    continue;
                }
                final String threadPoolName = ((NamedThreadFactory) tf).getName();
                log.debug("SpringAsyncThreadsMonitor threadPoolTaskExecutor: {}, {}, {}",
                    threadPoolName, e.getActiveCount(), e.getPoolSize());
            }
        } catch (Exception e) {
            log.error(METRIC_NAME + " monitorException");
        }
    }

}
