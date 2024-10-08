package com.github.zzf.actuator.rpc.http.provider.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 迁移到 filter 实现
        // traceId
        // registry.addInterceptor(new WebLogMdcHandlerInterceptor());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedMethods("*") // allow all method
            //.allowedOrigins("https://mp-wx.iot.chuanshi.ltd", "http://localhost:5173")
            .allowedHeaders("*")
            .allowCredentials(false)
            .maxAge(3600)
        ;
    }
}
