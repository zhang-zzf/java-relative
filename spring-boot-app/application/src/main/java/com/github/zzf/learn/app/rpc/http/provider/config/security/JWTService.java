package com.github.zzf.learn.app.rpc.http.provider.config.security;

import static cn.hutool.jwt.RegisteredPayload.SUBJECT;
import static java.nio.charset.StandardCharsets.UTF_8;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import com.github.zzf.learn.app.common.ConfigService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-10-08
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JWTService {

    private final ConfigService configService;

    public String jwtToken(String username, List<String> auth) {
        Date now = new Date();
        String encryptUsername = SecureUtil
            .aes(configService.getUsernameEncryptKey())
            .encryptBase64(username, UTF_8);
        return JWT.create().setSubject(encryptUsername)
            .setPayload("auth", auth)
            .setIssuedAt(now).setNotBefore(now)
            .setExpiresAt(DateUtil.offsetDay(now, configService.queryJWTTokenPeriod()))
            .setKey(configService.queryJWTTokenKey())
            .sign();
    }

    public UsernamePasswordAuthenticationToken parseToken(String token) {
        JWT jwt = JWT.of(token).setKey(configService.queryJWTTokenKey());
        JWTValidator.of(jwt).validateAlgorithm().validateDate();// 校验 sign 及 token 有效期
        String username = SecureUtil.aes(configService.getUsernameEncryptKey())
            .decryptStr(jwt.getPayload(SUBJECT).toString(), UTF_8);
        // authenticated
        return UsernamePasswordAuthenticationToken
            .authenticated(username, null, queryAuth(jwt));
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
