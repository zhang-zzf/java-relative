package com.github.zzf.learn.app.rpc.http.provider.station;

import static com.github.zzf.learn.app.rpc.http.provider.station.StationController.DomainMapper.INSTANCE;
import static java.util.stream.Collectors.toList;

import com.github.zzf.learn.app.rpc.http.provider.station.dto.StationDto;
import com.github.zzf.learn.app.station.StationService;
import com.github.zzf.learn.app.station.model.Station;
import com.github.zzf.learn.app.station.model.StationIdList;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-03-08
 */
@RestController
@RequestMapping("/api/v1/stations")
@Slf4j
@RequiredArgsConstructor
@Validated
public class StationController {

    final DomainMapper mapper = INSTANCE;

    final StationService stationService;

    @GetMapping("")
    public List<StationDto> query(@RequestParam @Size(max = 2) List<@NotNull Long> idList) {
        StationIdList idSet = StationIdList.builder().idSet(new HashSet<>(idList)).build();
        return stationService.queryBy(idSet).stream()
            .map(mapper::toDto)
            .collect(toList());
    }

    @GetMapping("/{id}")
    public StationDto queryById(@PathVariable Long id) {
        return mapper.toDto(stationService.queryById(id));
    }

    // todo RESTFul 如何表达
    @GetMapping("/ids")
    public List<Long> queryIdList() {
        return stationService.queryIdList().stream().map(Station::getId).toList();
    }

    @Mapper
    interface DomainMapper {
        DomainMapper INSTANCE = Mappers.getMapper(DomainMapper.class);

        StationDto toDto(Station domain);

    }
}
