package com.github.zzf.actuator.rpc.http.provider.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import com.github.zzf.actuator.common.ConfigService;
import com.github.zzf.actuator.rpc.http.provider.user.UserController;
import com.github.zzf.actuator.user.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)// Method Security
public class SpringSecurityConfiguration {

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JWTService jwtService) throws Exception {
        // permitAll 登陆/匿名 都可以 -> anonymous and authenticated
        // anonymous 必须时匿名，登陆的用户会授权失败
        http.securityMatcher("/api/**")
            .authorizeHttpRequests((requests) -> requests
                // 权限配置按从上到下的顺序依次校验
                .requestMatchers(HttpMethod.GET, "/api/users/*/*/token").permitAll()// 登陆
                .requestMatchers(HttpMethod.GET, "/api/users/card/*/status").permitAll()// 登陆
                .requestMatchers("/api/users/auth/*").permitAll()// 登陆
                .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()// 登陆
                .requestMatchers(HttpMethod.POST, "/api/users/sms/*").anonymous()// 短信验证码
                .requestMatchers(HttpMethod.POST, "/api/users").anonymous()// 用户注册
                //.requestMatchers("/api/admin/**").hasRole("ADMIN")// ant 语法
                //.requestMatchers(HttpMethod.GET, "/**").anonymous()
                .requestMatchers(HttpMethod.POST, "/api/cards/*/_verified").anonymous()// 卡实名认证后的回调接口
                .anyRequest().authenticated() // 任何人都可以访问，但必须要登陆
            );
        // http.formLogin(withDefaults());
        // 添加 jwt token filter to the filter chain, just before the basic authentication filter
        http.addFilterBefore(new JWTAuthenticationFilter(jwtService), BasicAuthenticationFilter.class);
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

    @Bean
    public UserDetailsService userDetailsService(UserController userController, ConfigService configService) {
        return username -> {
            Optional<User> dbUser = userController.user(username);
            if (dbUser.isEmpty()) {
                throw new UsernameNotFoundException(username);
            }
            User u = dbUser.get();
            List<SimpleGrantedAuthority> authorities = configService.userAuthority(username).stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
            return new org.springframework.security.core.userdetails.User(u.getUsername(), u.getPassword(),
                authorities);
        };
    }

}
