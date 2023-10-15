package com.github.learn.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
public class DateTimeBean2 {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    Date createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    LocalDateTime updatedAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate localDate;
    @JsonFormat(pattern = "HH:mm:ss.SSSSSS")
    LocalTime localTime;
}
