package com.github.zzf.dd.repo.mysql.iot_card.entity;

import java.util.Date;
import lombok.Data;

/**
  * @author : zhanfeng.zhang@icloud.com
  * @date : 2025-09-11
  */
@Data
public class Events {
    private Integer id;

    private Date dsLocalTime;

    private String tz;

    private Date dsUtcTime;

    private Date tsUtcTime;

    private Date dsCreatedAt;

    private Date dsUpdatedAt;

    private Date tsCreatedAt;

    private Date tsUpdatedAt;
}