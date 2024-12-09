package com.github.zzf.dd.user.event;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public interface UserEventProducer {
    // 触发流程
    // 1. ...
    // 2. ...
    void produce(@NotNull @Valid UserCreatedEvent e);

}
