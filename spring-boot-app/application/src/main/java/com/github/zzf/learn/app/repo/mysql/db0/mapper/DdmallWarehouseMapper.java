package com.github.zzf.learn.app.repo.mysql.db0.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.zzf.learn.app.repo.mysql.db0.entity.DdmallWarehouse;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-03-09
 */
@Mapper
public interface DdmallWarehouseMapper extends BaseMapper<DdmallWarehouse> {

    List<DdmallWarehouse> selectByIdList(@Param("idSet") Collection<Long> idSet);

    List<Long> pageGetId(@Param("startId") Long startId, @Param("pageSize") int pageSize);

    // todo Mybatis 如何利用 Map
    // todo Mybatis 如何利用 Page
    List<Long> queryPageBy(
        @Param("parameters") Map<String, String> parameters,
        @Param("pageable") Pageable pageable);

    Integer queryCountBy(@Param("parameters") Map<String, String> parameters);
}