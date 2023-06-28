package com.github.learn.spring.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/14
 */
@Service
public class EventBusService implements ApplicationEventPublisherAware {

  ApplicationEventPublisher publisher;

  public void publish(Object event) {
    publisher.publishEvent(event);
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.publisher = applicationEventPublisher;
  }
}
