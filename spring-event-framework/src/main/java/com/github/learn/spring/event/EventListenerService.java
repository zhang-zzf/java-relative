package com.github.learn.spring.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/14
 */
@Service
@Slf4j
public class EventListenerService {

  @EventListener
  public void handleEvent(Event event) {
    log.info("recv event: {}", event);
  }

}
