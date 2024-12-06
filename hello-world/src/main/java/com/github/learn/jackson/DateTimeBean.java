package com.github.learn.jackson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import lombok.Data;

@Data
public class DateTimeBean {
    Date createdAt;
    LocalDateTime updatedAt;
    LocalDate localDate;
    LocalTime localTime;
}
