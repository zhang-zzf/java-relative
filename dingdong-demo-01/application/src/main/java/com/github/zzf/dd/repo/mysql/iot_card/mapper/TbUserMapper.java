package com.github.zzf.dd.repo.mysql.iot_card.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.zzf.dd.repo.mysql.iot_card.entity.TbUser;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 */
public interface TbUserMapper extends BaseMapper<TbUser> {

    TbUser selectByUserNo(@Param("userNo") String userNo);
}