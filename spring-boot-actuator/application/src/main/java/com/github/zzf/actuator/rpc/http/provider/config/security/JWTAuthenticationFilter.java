package com.github.zzf.actuator.rpc.http.provider.config.security;

import static cn.hutool.jwt.RegisteredPayload.SUBJECT;
import static java.nio.charset.StandardCharsets.UTF_8;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import com.github.zzf.actuator.common.ConfigService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * watch out: the filter should not be managed by spring context.
 */
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHENTICATION_SCHEME_BASIC = "Bearer";

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource
        = new WebAuthenticationDetailsSource();
    private final SecurityContextHolderStrategy securityContextHolderStrategy
        = SecurityContextHolder.getContextHolderStrategy();

    private final ConfigService configService;

    public JWTAuthenticationFilter(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        UsernamePasswordAuthenticationToken auth = convert(request);
        if (auth == null) {
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(auth);
        this.securityContextHolderStrategy.setContext(context);
        filterChain.doFilter(request, response);
    }

    public UsernamePasswordAuthenticationToken convert(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            return null;
        }
        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_BASIC)) {
            return null;
        }
        try {
            JWT jwt = JWT.of(header.substring(7)).setKey(configService.queryJWTTokenKey());
            JWTValidator.of(jwt).validateAlgorithm().validateDate();// 校验 sign 及 token 有效期
            String username = SecureUtil.aes(configService.getUsernameEncryptKey())
                .decryptStr(jwt.getPayload(SUBJECT).toString(), UTF_8);
            // authenticated
            UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken
                .authenticated(username, null, queryAuth(jwt));
            result.setDetails(this.authenticationDetailsSource.buildDetails(request));
            return result;
        } catch (Exception e) {
            log.error("jwt convert to Authentication failed", e);
            return null;
        }
    }

    private static List<SimpleGrantedAuthority> queryAuth(JWT jwt) {
        try {
            List<String> auth = jwt.getPayload()
                .getClaimsJson().getBeanList("auth", String.class);
            if (auth != null) {
                return auth
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            }
        } catch (Exception e) {// ignore
            log.error("unExpected exception", e);
        }
        return new ArrayList<>();
    }

}
