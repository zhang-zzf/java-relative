package com.github.zzf.dd.rpc.http.provider.user.dto;

import com.github.zzf.dd.user.model.User;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
