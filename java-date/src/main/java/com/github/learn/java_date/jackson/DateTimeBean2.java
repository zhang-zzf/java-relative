package com.github.learn.java_date.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import lombok.Data;

@Data
public class DateTimeBean2 {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    Date createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Shanghai")
    Date createdAt2;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    Date createdAt3;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    LocalDateTime updatedAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate localDate;
    @JsonFormat(pattern = "HH:mm:ss.SSSSSS")
    LocalTime localTime;
}
