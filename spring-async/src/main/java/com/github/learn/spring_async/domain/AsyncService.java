package com.github.learn.spring_async.domain;

import com.github.learn.spring_async.infra.context.Context;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/10
 */
@Service
@Slf4j
public class AsyncService {

  @Async
  public void methodA() {
    log.info("methodA => {}, {}", Thread.currentThread().getName(), Context.USER_ID.get());
  }

}
