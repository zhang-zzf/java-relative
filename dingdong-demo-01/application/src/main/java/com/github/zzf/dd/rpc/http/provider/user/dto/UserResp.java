package com.github.zzf.dd.rpc.http.provider.user.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserResp {
    private String type;
    private String username;
    private LocalDateTime createdAt;
    private String token;
}
