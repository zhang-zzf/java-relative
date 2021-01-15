package com.feng.learn.redis.lbs.demo.domain.repo;

import com.feng.learn.redis.lbs.demo.domain.model.Location;

import java.util.List;

/**
 * @author zhanfeng.zhang
 * @date 2020/12/11
 */
public interface LocationRepository {

    void updateLocation(List<Location> locations);

    List<Location> batchGetById(List<Long> ids);

    /**
     * 中心点+半径 GEO 搜索
     * <p>按距离中心点的距离自然排序</p>
     *
     * @param lat    维度
     * @param lon    经度
     * @param radius 半径, 单位：米
     */
    List<Long> searchGeo(double lat, double lon, long radius);

}
