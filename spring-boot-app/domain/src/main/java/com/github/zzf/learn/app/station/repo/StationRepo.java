package com.github.zzf.learn.app.station.repo;

import com.github.zzf.learn.app.common.SearchAfter;
import com.github.zzf.learn.app.station.model.Station;
import com.github.zzf.learn.app.station.model.StationIdList;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    /**
     * 方法可以使用 searchAfter 替换
     */
    // todo iterator 只返回 id list
    @NotNull
    // Iterator<List<Long>> iterator();

    // todo repo 只返回 id list 方便性能优化
    Page<Long> queryPageBy(Map<String, String> parameters, Pageable pageable);

    /**
     * <pre>
     *      queryList / searchAfter 2 选 1
     *      DB 数量少 (如 1W) 使用 queryList; DB 数量多 (如 1B) 使用 searchAfter
     * </pre>
     *
     */
    @NotNull
    List<Station> queryList();

    Stream<Station> searchAfter(SearchAfter req);
}
