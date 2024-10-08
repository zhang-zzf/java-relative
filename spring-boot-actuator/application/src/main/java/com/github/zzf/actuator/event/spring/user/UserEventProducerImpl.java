package com.github.zzf.actuator.event.spring.user;

import com.github.zzf.actuator.user.event.UserCreatedEvent;
import com.github.zzf.actuator.user.event.UserEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserEventProducerImpl implements UserEventProducer {
    final ApplicationEventPublisher publisher;

    @Override
    public void produce(UserCreatedEvent userCreatedEvent) {
        publisher.publishEvent(userCreatedEvent);
    }

}