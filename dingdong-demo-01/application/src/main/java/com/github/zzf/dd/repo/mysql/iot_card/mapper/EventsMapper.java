package com.github.zzf.dd.repo.mysql.iot_card.mapper;

import com.github.zzf.dd.repo.mysql.iot_card.entity.Events;
import java.util.List;

/**
  * @author : zhanfeng.zhang@icloud.com
  * @date : 2025-09-11
  */
public interface EventsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Events record);

    Events selectByPrimaryKey(Integer id);

    Events selectLatest();
}