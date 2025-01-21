package com.github.zzf.learn.user.repo;

import com.github.zzf.learn.user.model.User;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public interface UserRepo {

    @Nullable
    User queryUserByUserNo(@NotNull String userNo);

    @NotNull
    long tryCreateUser(@NotNull User user);

}
