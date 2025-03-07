package com.github.zzf.learn.app.rpc.http.provider.user.dto;

import com.github.zzf.learn.app.user.model.User;
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
