package com.github.zzf.learn.user.event;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
