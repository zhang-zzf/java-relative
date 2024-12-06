package com.github.zzf.actuator.config.log;

import static com.github.zzf.actuator.common.log.LogTracer.X_TRACE_ID;

import com.github.zzf.actuator.common.log.LogTracer;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
