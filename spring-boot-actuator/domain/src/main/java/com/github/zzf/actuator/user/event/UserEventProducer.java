package com.github.zzf.actuator.user.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public interface UserEventProducer {
    // 触发流程
    // 1. ...
    // 2. ...
    void produce(@NotNull @Valid UserCreatedEvent e);

}
