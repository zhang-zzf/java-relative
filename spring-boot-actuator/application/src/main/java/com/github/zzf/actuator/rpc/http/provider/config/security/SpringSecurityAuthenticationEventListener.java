package com.github.zzf.actuator.rpc.http.provider.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * 参考 {@link SecurityAutoConfiguration}
 */
@Component
@Slf4j
public class SpringSecurityAuthenticationEventListener {


    @EventListener
    @Async
    public void onAuthenticationEvent(AbstractAuthenticationEvent e) {
        // todo 记录到 db
        if (e instanceof AuthenticationSuccessEvent se) {
            log.debug("onAuthenticationEvent success -> {}", se.getAuthentication().getName());
        }
        else if (e instanceof AbstractAuthenticationFailureEvent fe) {
            log.info("onAuthenticationEvent failed -> {}", fe.getAuthentication().getName());
            log.error("onAuthenticationEvent failed", fe.getException());
        }
    }

}
