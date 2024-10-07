package com.github.zzf.actuator.event.spring.credit;

import static com.github.zzf.actuator.utils.LogUtils.json;

import com.github.zzf.actuator.user.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component("userCreatedListener/credit")
@RequiredArgsConstructor
@Slf4j
public class UserCreatedListener {

    // final UserCreditService userCreditService;

    @EventListener
    public void onUserCreated(UserCreatedEvent event) {
        log.info("onUserCreated event -> {}", json(event));
        // userCreditService.createUserAccount(new CreditCreateReq(event.getUserNo()));
    }
}
