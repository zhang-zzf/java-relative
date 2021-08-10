package com.github.learn.spring_async.infra.spring.async;


import com.github.learn.spring_async.infra.spring.async.SpringAsyncConfig.NamedThreadFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @author zhanfeng.zhang
 * @date 2020/09/18
 */
@Component
@Slf4j
public class SpringAsyncThreadDynamicChanger {

    private final Map<String, ThreadPoolTaskExecutor> NAME_TO_POOL;

    public SpringAsyncThreadDynamicChanger(List<ThreadPoolTaskExecutor> allExecutorInSpringContext) {
        NAME_TO_POOL = new HashMap<>(allExecutorInSpringContext.size());
        for (ThreadPoolTaskExecutor t : allExecutorInSpringContext) {
            ThreadFactory tf = t.getThreadPoolExecutor().getThreadFactory();
            if (tf instanceof NamedThreadFactory) {
                NAME_TO_POOL.putIfAbsent(((NamedThreadFactory) tf).getName(), t);
                continue;
            }
            log.error("SpringAsyncThreadDynamicChanger not a NamedThreadFactory: {}", t);
        }
        log.info("SpringAsyncThreadDynamicChanger controlled threadPools: {}", NAME_TO_POOL);
    }

    /**
     * 配置中心 callback
     *
     * @param name 线程池的名字
     * @param config 配置
     */
    void updateThreadPool(String name, ThreadPoolConfig config) {
        if (!config.checked()) {
            log.error("illegalThreadPoolConfig {}", config);
            return;
        }
        ThreadPoolTaskExecutor pool = NAME_TO_POOL.get(name);
        pool.setCorePoolSize(config.getCore());
        pool.setMaxPoolSize(config.getMax());
        pool.setKeepAliveSeconds(config.getKeepAliveSeconds());
        log.warn("updateThreadPool: {}, {} ", name, config);
    }

    @Data
    public static class ThreadPoolConfig {

        private int core;
        private int max;
        private int keepAliveSeconds;

        public boolean checked() {
            if (core > 0 && max > 0 && max >= core && keepAliveSeconds > 0) {
                return true;
            }
            return false;
        }
    }

}

