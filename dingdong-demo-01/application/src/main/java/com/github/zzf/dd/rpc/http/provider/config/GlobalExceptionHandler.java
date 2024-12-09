package com.github.zzf.dd.rpc.http.provider.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler implements InitializingBean {

    private MessageSource messageSource;

    @Override
    public void afterPropertiesSet() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename("http/i18n/Exceptions");
        ms.setUseCodeAsDefaultMessage(true);
        this.messageSource = ms;
    }
}
