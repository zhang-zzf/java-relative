package com.github.zzf.learn.user;

import static com.github.zzf.learn.common.Common.MOBILE_REGEXP_PATTERN;
import static com.github.zzf.learn.utils.LogUtils.json;

import com.github.zzf.learn.user.event.UserCreatedEvent;
import com.github.zzf.learn.user.event.UserEventProducer;
import com.github.zzf.learn.user.model.User;
import com.github.zzf.learn.user.model.UserRegisterReq;
import com.github.zzf.learn.user.repo.UserRepo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserService {

    final UserMapper mapper = UserMapper.INSTANCE;

    final UserRepo userRepo;
    final UserEventProducer userEventProducer;

    /**
     * 用户注册
     *
     * @param req 用户名密码
     * @return 用户ID
     */
    @NotNull
    public User register(@Valid UserRegisterReq req) {
        req.verify();
        User user = mapper.toDomain(req);
        // todo 注册前验证, 手机验证码等
        //
        String userNo = user.queryUserNo();// userNo 生成算法保留在 业务层
        user.setUserNo(userNo);
        Optional<User> existUser = queryUser(userNo);
        if (existUser.isPresent()) {
            log.info("register -> idempotent {}", json(user));
            return existUser.get();
        }
        // encrypt password
        user.setPassword("{noop}" + user.getPassword());
        userRepo.tryCreateUser(user);// 尝试保存用户信息
        userEventProducer.produce(new UserCreatedEvent(userNo));// trigger UserCreatedEvent
        return user;
    }

    public @NotNull Optional<User> queryUserByUserName(@NotNull String userName) {
        if (MOBILE_REGEXP_PATTERN.matcher(userName).matches()) {
            return queryUser(User.fromMobile(userName).queryUserNo());
        }
        else {
            return queryUser(User.fromCard(userName).queryUserNo());
        }
    }

    public @NotNull Optional<User> queryUser(@NotNull String userNo) {
        return Optional.ofNullable(userRepo.queryUserByUserNo(userNo));
    }

    @Mapper
    public interface UserMapper {
        UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

        @Mapping(target = "userNo", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "username", expression = "java(req.queryUsername())")
        User toDomain(UserRegisterReq req);
    }
}