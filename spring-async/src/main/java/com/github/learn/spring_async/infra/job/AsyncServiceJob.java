package com.github.learn.spring_async.infra.job;

import com.github.learn.spring_async.domain.AsyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@Component
@RequiredArgsConstructor
public class AsyncServiceJob {

    final AsyncService asyncService;

    @Scheduled(fixedDelay = 8000)
    public void schedule() {
        asyncService.methodA();
    }

}
