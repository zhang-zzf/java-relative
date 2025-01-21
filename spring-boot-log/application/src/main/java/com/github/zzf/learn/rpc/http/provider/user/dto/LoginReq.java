package com.github.zzf.learn.rpc.http.provider.user.dto;

import com.github.zzf.learn.user.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginReq {
    @NotNull
    @Pattern(regexp = User.USER_TYPE_PATTERN)
    String type;
    @NotNull
    String username;
    @NotNull
    String password;
}
