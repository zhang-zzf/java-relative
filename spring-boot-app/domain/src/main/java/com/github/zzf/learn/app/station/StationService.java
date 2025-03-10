package com.github.zzf.learn.app.station;

import com.github.zzf.learn.app.station.model.Station;
import com.github.zzf.learn.app.station.model.StationIdList;
import com.github.zzf.learn.app.station.repo.StationRepo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-03-08
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class StationService {

    final StationRepo stationRepo;

    /**
     * 批量查询接口
     */
    public @NotNull List<Station> queryBy(@NotNull @Valid StationIdList idList) {
        return stationRepo.queryBy(idList);
    }

    public @NotNull List<Station> queryIdList() {
        return stationRepo.queryList();
    }

    public Station queryById(Long id) {
        return stationRepo.queryBy(id);
    }

}
