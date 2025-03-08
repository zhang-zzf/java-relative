package com.github.zzf.learn.app.station.repo;

import com.github.zzf.learn.app.station.model.Station;
import com.github.zzf.learn.app.station.model.StationIdList;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-03-08
 */
public interface StationRepo {
    @NotNull
    List<Station> query(@NotNull @Valid StationIdList idList);

    @NotNull List<Long> queryIdList();

}
