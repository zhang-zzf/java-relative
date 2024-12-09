package com.github.zzf.dd.config.log;


import javax.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-10-08
 */
@Configuration
@Slf4j
public class ServletLogConfiguration {

    @Bean
    public FilterRegistrationBean<Filter> logTracerFilter() {
        return new FilterRegistrationBean<>() {
            @Override
            public Filter getFilter() {
                // log filter should be the head of the chain
                setOrder(Integer.MIN_VALUE);
                return new ServletLogFilter();
            }
        };
    }
}
