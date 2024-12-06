package com.github.zzf.actuator.user.model;

import static com.github.zzf.actuator.common.Common.MOBILE_REGEXP;
import static com.github.zzf.actuator.user.model.User.USER_TYPE_PATTERN;
import static com.github.zzf.actuator.user.model.User.cardUser;
import static com.github.zzf.actuator.user.model.User.mobileUser;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterReq {
    @NotEmpty
    @Pattern(regexp = USER_TYPE_PATTERN)
    String type;
    @Size(min = 9, max = 10)
    String cardNo;
    @Size(min = 11, max = 11)
    @Pattern(regexp = MOBILE_REGEXP)
    String mobile;
    @NotEmpty
    @Size(min = 4, max = 16)
    String password;
    @NotEmpty
    @Size(min = 4, max = 8)
    String verificationCode;

    public void verify() {
        if (cardNo == null && mobile == null) {
            throw new IllegalArgumentException("CARD_AND_MOBILE_ARE_BOTH_NULL");
        }
    }

    public String queryUsername() {
        if (mobileUser(type)) {
            return mobile;
        }
        else if (cardUser(type)) {
            return cardNo;
        }
        throw new IllegalArgumentException();
    }

}
