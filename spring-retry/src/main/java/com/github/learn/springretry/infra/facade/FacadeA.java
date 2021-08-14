package com.github.learn.springretry.infra.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/14
 */
@Service
@Slf4j
public class FacadeA {

    /**
     * 重试全部失败后，执行 empty 方法
     */
    @Retryable(recover = "empty", backoff = @Backoff(delay = 100))
    public void methodA() {
        log.info("facadeA.methodA()");
        throw new IllegalArgumentException();
    }

    @Recover
    void empty(Throwable throwable) {
        log.info("", throwable);
    }

}
