package com.github.zzf.learn.app.common;

import com.github.zzf.learn.app.common.ConfigService.UserAuthorities;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Configuration
@Slf4j
@Validated
@RequiredArgsConstructor
@EnableConfigurationProperties(UserAuthorities.class)
public class ConfigService {

    @Value("${verification.code.period:600}")
    Long verificationCodePeriod;

    @Value("${verification.code.limit:90}")
    Integer verificationCodeLimit;

    @Value("${jwt.token.key:n909LRkfBMZq}")
    String jwtTokenKey;

    @Value("${jwt.token.period:180}")
    Integer jwtTokenPeriod;

    @Value("${username.encrypt.key:GpyuEvTWOoxCSyL5}")
    String usernameEncryptKey;

    final UserAuthorities userAuthorities;

    public long queryVerificationCodePeriod() {
        return verificationCodePeriod;
    }

    public int queryVerificationCodeLimit() {
        return verificationCodeLimit;
    }

    public byte[] queryJWTTokenKey() {
        return jwtTokenKey.getBytes(StandardCharsets.UTF_8);
    }

    public int queryJWTTokenPeriod() {
        return jwtTokenPeriod;
    }

    public List<String> userAuthority(String username) {
        if (userAuthorities != null) {
            return userAuthorities.getConfigs().getOrDefault(username, List.of());
        }
        return List.of();
    }

    public boolean userIsAdmin(String username) {
        return userAuthority(username).contains("ROLE_ADMIN");
    }

    public byte[] getUsernameEncryptKey() {
        return usernameEncryptKey.getBytes(StandardCharsets.UTF_8);
    }

    public int getQueryStationByIdListCacheBatchSize() {
        return 5000;
    }

    public boolean isCheckStationIdExistsBeforeQuery() {
        return true;
    }

    @Data
    @ConfigurationProperties(prefix = "user.authority")
    public static class UserAuthorities {
        Map<String, List<String>> configs;
    }
}
