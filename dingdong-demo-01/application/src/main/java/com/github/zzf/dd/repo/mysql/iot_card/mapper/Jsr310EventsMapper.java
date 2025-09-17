package com.github.zzf.dd.repo.mysql.iot_card.mapper;

import com.github.zzf.dd.repo.mysql.iot_card.entity.Jsr310Events;

/**
  * @author : zhanfeng.zhang@icloud.com
  * @date : 2025-09-11
  */
public interface Jsr310EventsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Jsr310Events record);

    Jsr310Events selectByPrimaryKey(Integer id);

    Jsr310Events selectLatest();
}