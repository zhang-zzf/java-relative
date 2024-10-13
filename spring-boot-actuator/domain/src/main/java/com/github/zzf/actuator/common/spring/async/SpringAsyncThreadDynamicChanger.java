package com.github.zzf.actuator.common.spring.async;


import static com.github.zzf.actuator.utils.LogUtils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.zzf.actuator.common.spring.async.SpringAsyncConfig.NamedThreadFactory;
import com.github.zzf.actuator.utils.LogUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @author zhanfeng.zhang
 * @date 2020/09/18
 */
@Component
@Slf4j
public class SpringAsyncThreadDynamicChanger {

    /**
     * 依赖构造器注入
     */
    private final Map<String, ThreadPoolTaskExecutor> NAME_TO_POOL;

    private final Environment environment;

    @EventListener
    public void springEnvUpdate(EnvironmentChangeEvent e) {
        for (String key : e.getKeys()) {
            if (!NAME_TO_POOL.containsKey(key)) {
                continue;
            }
            ThreadPoolConfig newConfig = getThreadPoolConfig(key);
            log.info("receive ThreadPool change event -> {}: {}", key, json(newConfig));
            configThreadPool(key, newConfig);
        }

    }

    private ThreadPoolConfig getThreadPoolConfig(String key) {
        String property = environment.getProperty(key);
        ThreadPoolConfig newConfig = null;
        try {
            // todo 待优化
            newConfig = LogUtils.mapper.readValue(property, ThreadPoolConfig.class);
        } catch (Throwable ex) {
            // ignore
        }
        return newConfig;
    }

    public SpringAsyncThreadDynamicChanger(
        List<ThreadPoolTaskExecutor> allExecutorInSpringContext,
        Environment environment) {
        NAME_TO_POOL = new HashMap<>(allExecutorInSpringContext.size());
        this.environment = environment;
        for (ThreadPoolTaskExecutor t : allExecutorInSpringContext) {
            ThreadFactory tf = t.getThreadPoolExecutor().getThreadFactory();
            if (tf instanceof NamedThreadFactory) {
                String name = ((NamedThreadFactory) tf).getName();
                NAME_TO_POOL.putIfAbsent(name, t);
                configThreadPool(name, getThreadPoolConfig(name));
                continue;
            }
            log.error("SpringAsyncThreadDynamicChanger not a NamedThreadFactory: {}", t);
        }
        log.info("SpringAsyncThreadDynamicChanger controlled threadPools: {}", NAME_TO_POOL);
    }

    /**
     * 配置中心 callback
     *
     * @param name   线程池的名字
     * @param config 配置
     */
    public void configThreadPool(String name, ThreadPoolConfig config) {
        if (config == null) {
            return;
        }
        if (!config.verify()) {
            log.error("illegalThreadPoolConfig -> {}", json(config));
            return;
        }
        ThreadPoolTaskExecutor pool = NAME_TO_POOL.get(name);
        pool.setCorePoolSize(config.getCore());
        pool.setMaxPoolSize(config.getMax());
        pool.setKeepAliveSeconds(config.getKeepAliveSeconds());
        log.warn("ThreadPool updated -> name: {}, config: {} ", name, json(config));
    }

    @Data
    public static class ThreadPoolConfig {

        private int core;
        private int max;
        private int keepAliveSeconds;

        public boolean verify() {
            if (core > 0 && max > 0 && max >= core && keepAliveSeconds > 0) {
                return true;
            }
            return false;
        }
    }

}

