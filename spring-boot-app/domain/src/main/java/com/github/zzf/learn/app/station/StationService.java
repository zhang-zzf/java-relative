package com.github.zzf.learn.app.station;

import com.github.zzf.learn.app.common.SearchAfter;
import com.github.zzf.learn.app.station.model.Station;
import com.github.zzf.learn.app.station.model.StationIdList;
import com.github.zzf.learn.app.station.repo.StationRepo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public Page<Station> queryPageBy(Map<String, String> parameters, Pageable pageable) {
        Page<Long> idPage = stationRepo.queryPageBy(parameters, pageable);
        List<Long> idList = idPage.getContent();
        // watch out: StationIdList 本身是无序的
        StationIdList idSet = new StationIdList(new HashSet<>(idList));
        // watch out: 分页是按有排序规则的
        List<Station> stationList = queryBy(idSet)
            .stream()
            .sorted(Comparator.comparing(s -> idList.indexOf(s.getId())))
            .toList();
        return new PageImpl<>(stationList, pageable, idPage.getTotalPages());
    }

    public Stream<Station> searchAfter(SearchAfter req) {
       return stationRepo.searchAfter(req);
    }
}
