package com.github.zzf.learn.app.repo;

import com.github.zzf.learn.app.station.model.Station;
import com.github.zzf.learn.app.station.model.StationIdList;
import com.github.zzf.learn.app.station.repo.StationRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-03-08
 */
@Repository
@Slf4j
@Validated
@RequiredArgsConstructor
public class StationRepoMySQLImpl implements StationRepo {
    @Override
    public List<Station> query(StationIdList idList) {
        return List.of();
    }

    @Override
    public List<Long> queryIdList() {
        return List.of();
    }
}
