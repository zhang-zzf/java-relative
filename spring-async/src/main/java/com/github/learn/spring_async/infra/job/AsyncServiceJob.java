package com.github.learn.spring_async.infra.job;


import static com.github.learn.spring_async.infra.context.Context.USER_ID;

import com.github.learn.spring_async.domain.AsyncService;
import com.github.learn.spring_async.infra.context.Context;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AsyncServiceJob {

    final AsyncService asyncService;

    @Scheduled(fixedDelay = 8000)
    public void schedule() {
        Context.USER_ID.set(999L);
        log.info("{} => {}", Thread.currentThread().getName(), USER_ID.get());
        asyncService.methodA();
        USER_ID.remove();
    }

}
