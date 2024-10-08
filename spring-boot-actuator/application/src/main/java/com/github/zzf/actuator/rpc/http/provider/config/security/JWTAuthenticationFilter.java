package com.github.zzf.actuator.rpc.http.provider.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHENTICATION_SCHEME_BASIC = "Bearer";

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource
        = new WebAuthenticationDetailsSource();
    private final SecurityContextHolderStrategy securityContextHolderStrategy
        = SecurityContextHolder.getContextHolderStrategy();

    private final JWTService jwtService;

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
            UsernamePasswordAuthenticationToken result = jwtService.parseToken(header.substring(7));
            // authenticated
            result.setDetails(this.authenticationDetailsSource.buildDetails(request));
            return result;
        } catch (Exception e) {
            log.error("jwt convert to Authentication failed", e);
            return null;
        }
    }

}
