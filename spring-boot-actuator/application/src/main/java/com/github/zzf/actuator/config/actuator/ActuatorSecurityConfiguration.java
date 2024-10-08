package com.github.zzf.actuator.config.actuator;

import static org.springframework.security.config.Customizer.withDefaults;

import com.github.zzf.actuator.common.ConfigService;
import com.github.zzf.actuator.rpc.http.provider.config.security.JWTAuthenticationFilter;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)// Method Security
public class ActuatorSecurityConfiguration {

    @Bean
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http, ConfigService configService)
        throws Exception {
        // permitAll 登陆/匿名 都可以 -> anonymous and authenticated
        // anonymous 必须是匿名，登陆的用户会授权失败
        http.securityMatcher(EndpointRequest.toAnyEndpoint()
                .excluding("health", "prometheus"))
            .authorizeHttpRequests((requests) -> requests.anyRequest().hasRole("ENDPOINT_ADMIN"));
        // 添加 jwt token filter to the filter chain, just before the basic authentication filter
        http.addFilterBefore(new JWTAuthenticationFilter(configService), BasicAuthenticationFilter.class);
        http.httpBasic(withDefaults())
            // if Spring MVC is on classpath and no CorsConfigurationSource is provided,
            // Spring Security will use CORS configuration provided to Spring MVC
            // {@link com.github.zzf.web.config.SpringMvcConfig}
            .cors(withDefaults())
            // 禁用 CSRF filter
            .csrf(AbstractHttpConfigurer::disable)
            // 禁用 logout filter
            .logout(AbstractHttpConfigurer::disable)
            // Stateless Session Management for restful api. restful api should not use any session
            .sessionManagement(smc -> smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ehc -> ehc
                // 403
                .accessDeniedHandler((req, resp, exception) -> resp.setStatus(HttpStatus.FORBIDDEN.value()))
                // 401
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            );
        return http.build();
    }

}
