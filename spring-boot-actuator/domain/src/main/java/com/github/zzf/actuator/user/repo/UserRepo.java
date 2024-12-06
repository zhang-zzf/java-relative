package com.github.zzf.actuator.user.repo;

import com.github.zzf.actuator.user.model.User;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public interface UserRepo {

    @Nullable
    User queryUserByUserNo(@NotNull String userNo);

    @NotNull
    long tryCreateUser(@NotNull User user);

}
