package com.github.zzf.learn.app.rpc.http.provider.station;

import static com.github.zzf.learn.app.rpc.http.provider.station.StationController.DomainMapper.INSTANCE;
import static com.github.zzf.learn.app.utils.LogUtils.json;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.github.zzf.learn.app.rpc.http.provider.station.dto.StationDto;
import com.github.zzf.learn.app.station.StationService;
import com.github.zzf.learn.app.station.model.Station;
import com.github.zzf.learn.app.station.model.StationIdList;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
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

    @GetMapping
    public List<StationDto> queryList() {
        Set<Long> idSet = stationService.queryIdList().stream().map(Station::getId).collect(toSet());
        StationIdList stationIdList = StationIdList.builder().idSet(idSet).build();
        return stationService.queryBy(stationIdList).stream()
            .map(mapper::toDto)
            .collect(toList());
    }

    /**
     * 按查询条件分页查询
     */
    @GetMapping("/_search")
    public Page<StationDto> queryPage(
        @RequestParam Map<String, String> parameters,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "-updatedAt,-id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, toSort(sortBy));
        log.info("_search req -> params: {}, page: {}", parameters, json(pageable));
        Page<Station> pageData = stationService.queryPageBy(parameters, pageable);
        List<StationDto> stationDtoList = pageData.get().map(mapper::toDto).toList();
        return new PageImpl<>(stationDtoList, pageable, pageData.getTotalElements());
    }

    private Sort toSort(String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            return Sort.unsorted();
        }
        List<Order> orderList = Arrays.stream(sortBy.split(","))
            .map(String::trim)
            .map(this::toOrder)
            .toList();
        return Sort.by(orderList);
    }

    private Order toOrder(String s) {
        return s.startsWith("-") ? Order.desc(s.substring(1)) :
            s.startsWith("+") ? Order.asc(s.substring(1)) : Order.asc(s);
    }

    @GetMapping("/batch")
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
