package com.github.zzf.dd.repo.mysql.iot_card.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.Data;

/**
  * @author : zhanfeng.zhang@icloud.com
  * @date : 2025-09-11
  */
@Data
public class Jsr310Events {
    private Integer id;

    private LocalDateTime dsLocalTime;

    private String tz;

    private Instant dsUtcTime;

    private Instant tsUtcTime;

    // mysql:mysql-connector-java:8.0.22 下无法保存 ZonedDateTime
    // private ZonedDateTime tsUtcTime;
    // mysql:mysql-connector-java:8.0.22 下无法保存 OffsetDateTime
    // private OffsetDateTime tsUtcTime;

    private Instant dsCreatedAt;

    private Instant dsUpdatedAt;

    private Instant tsCreatedAt;

    private Instant tsUpdatedAt;
}