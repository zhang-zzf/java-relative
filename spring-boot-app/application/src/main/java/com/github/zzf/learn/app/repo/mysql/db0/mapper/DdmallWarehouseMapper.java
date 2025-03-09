package com.github.zzf.learn.app.repo.mysql.db0.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.zzf.learn.app.repo.mysql.db0.entity.DdmallWarehouse;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-03-09
 */
@Mapper
public interface DdmallWarehouseMapper extends BaseMapper<DdmallWarehouse> {

    List<DdmallWarehouse> selectByIdList(@Param("idSet") Collection<Long> idSet);

    List<Long> pageGetId(@Param("startId") Long startId, @Param("pageSize") int pageSize);
}