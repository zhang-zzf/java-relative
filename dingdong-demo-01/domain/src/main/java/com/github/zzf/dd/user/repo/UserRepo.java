package com.github.zzf.dd.user.repo;

import com.github.zzf.dd.user.model.User;
import javax.validation.constraints.NotNull;

public interface UserRepo {

    User queryUserByUserNo(@NotNull String userNo);

    long tryCreateUser(@NotNull User user);

}
