package com.github.zzf.learn.app.rpc.http.provider.user;

import static com.github.zzf.learn.app.user.model.User.USER_TYPE_PATTERN;
import static com.github.zzf.learn.app.utils.LogUtils.json;

import com.github.zzf.learn.app.common.ConfigService;
import com.github.zzf.learn.app.common.exception.ResourceNotFoundException;
import com.github.zzf.learn.app.rpc.http.provider.config.security.JWTService;
import com.github.zzf.learn.app.rpc.http.provider.user.dto.UserResp;
import com.github.zzf.learn.app.user.UserService;
import com.github.zzf.learn.app.user.model.User;
import com.github.zzf.learn.app.user.model.UserRegisterReq;
import jakarta.validation.constraints.Pattern;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/users")
public class UserController {
    final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    final DomainMapper mapper = DomainMapper.INSTANCE;

    private final ConfigService configService;

    private final UserService userService;

    private final JWTService jwtService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResp register(@RequestBody UserRegisterReq req) {
        log.info("register req -> {}", json(req));
        User user = userService.register(req);
        log.info("register resp -> {}", json(user));
        return mapper.toDto(user);
    }

    @GetMapping("/{type}/{username}/token")
    public String userToken(
        @PathVariable @Pattern(regexp = USER_TYPE_PATTERN) String type,
        @PathVariable String username,
        @RequestParam String password) {
        Optional<User> userOpt = user(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("USER_NOT_EXISTS");
        }
        if (!passwordEncoder.matches(password, userOpt.get().getPassword())) {
            throw new IllegalArgumentException("WRONG_PASSWORD");
        }
        return jwtService.jwtToken(username, configService.userAuthority(username));
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/{type}/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') && hasAuthority('ROLE_ENDPOINT_ADMIN') && #username == authentication.name")
    // 只有登陆用户可以获取自己的数据
    public UserResp user(
        @PathVariable @Pattern(regexp = USER_TYPE_PATTERN) String type,
        @PathVariable String username) {
        Optional<User> optionalUser = user(username);
        if (optionalUser.isEmpty()) {
            // 配合 com.github.zzf.config.GlobalExceptionHandler.handleResourceNotFoundException
            throw new ResourceNotFoundException();
        }
        return mapper.toDto(optionalUser.get());
    }

    public Optional<User> user(String username) {
        return userService.queryUserByUserName(username);
    }

    @Mapper
    public interface DomainMapper {
        DomainMapper INSTANCE = Mappers.getMapper(DomainMapper.class);

        UserResp toDto(User domain);
    }
}
