package com.github.zzf.dd.user.event;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent {
    final LocalDateTime timestamp = LocalDateTime.now();
    @NotNull
    String userNo;
}
