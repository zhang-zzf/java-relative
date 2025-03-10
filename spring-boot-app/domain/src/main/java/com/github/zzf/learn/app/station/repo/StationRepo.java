package com.github.zzf.learn.app.station.repo;

import com.github.zzf.learn.app.station.model.Station;
import com.github.zzf.learn.app.station.model.StationIdList;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.List;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-03-08
 */
public interface StationRepo {

    void save(Station station);

    @NotNull
    List<Station> queryBy(@NotNull @Valid StationIdList idList);

    @Nullable
    Station queryBy(@NotNull Long id);

    @NotNull
    List<Station> queryList();

    // todo iterator 只返回 id list
    @NotNull
    Iterator<List<Long>> iterator();
}
