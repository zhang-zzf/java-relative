package com.github.zzf.learn.app.repo;

import static com.github.zzf.learn.app.repo.StationRepoMySQLImpl.BEAN_NAME;
import static com.github.zzf.learn.app.repo.StationRepoMySQLImpl.DomainMapper.INSTANCE;
import static com.google.common.base.CaseFormat.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.github.zzf.learn.app.common.ConfigService;
import com.github.zzf.learn.app.common.SearchAfter;
import com.github.zzf.learn.app.repo.mysql.db0.entity.DdmallWarehouse;
import com.github.zzf.learn.app.repo.mysql.db0.mapper.DdmallWarehouseMapper;
import com.github.zzf.learn.app.station.model.Station;
import com.github.zzf.learn.app.station.model.StationIdList;
import com.github.zzf.learn.app.station.repo.StationRepo;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
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
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-03-08
 */
@Repository(BEAN_NAME)
@Slf4j
@Validated
@RequiredArgsConstructor
public class StationRepoMySQLImpl implements StationRepo {

    public static final String BEAN_NAME = "stationRepoMySQLImpl";

    final DomainMapper mapper = INSTANCE;
    final DdmallWarehouseMapper ddmallWarehouseMapper;
    final ConfigService configService;

    @Override
    public void save(Station station) {
        if (station.getId() == null) {
            ddmallWarehouseMapper.insert(mapper.toPO(station));
        }
        else {
            ddmallWarehouseMapper.updateById(mapper.toPO(station));
        }
    }

    @Override
    public List<Station> queryBy(StationIdList idList) {
        List<DdmallWarehouse> dbData = ddmallWarehouseMapper.selectByIdList(idList.getIdSet());
        return mapper.toDomain(dbData);
    }

    @Nullable
    @Override
    public Station queryBy(Long id) {
        return queryBy(new StationIdList(Set.of(id))).stream().findAny().orElse(null);
    }

    @Override
    public List<Station> queryList() {
        Iterator<List<Long>> it = iterator();
        List<Station> ret = new ArrayList<>();
        while (it.hasNext()) {
            ret.addAll(mapper.toDomain(ddmallWarehouseMapper.selectByIdList(it.next())));
        }
        return ret;
    }

    public Iterator<List<Long>> iterator() {
        return new Iterator<>() {
            // 配置中心动态更改大小
            private static final int ONE_MILLION = 1000000;
            // 配置中心动态更改大小
            final int pageSize = 1000;
            List<Long> next = null;
            int loop = 0;

            @Override
            public boolean hasNext() {
                if (loop++ > ONE_MILLION) {// 200,000,000
                    // logEvent("Iterator", "LoopOverflow", next);// 异常退出，打点监控
                    return false;
                }
                next = ddmallWarehouseMapper.pageGetId(next == null ? -1L : next.get(next.size() - 1), pageSize);
                return !next.isEmpty();
            }

            @Override
            public List<Long> next() {
                return next;
            }
        };
    }

    @Override
    public Page<Long> queryPageBy(Map<String, String> parameters, final Pageable pageable) {
        // todo 优化方案， PageHelper / Mybatis Plus
        Integer total = ddmallWarehouseMapper.queryCountBy(parameters);
        if (total == 0) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
        Sort defaultSort = Sort.by(DESC, "updatedAt").and(Sort.by(DESC, "id"));
        List<Long> dbData = ddmallWarehouseMapper.queryPageBy(parameters, toMySQLPageable(pageable, defaultSort));
        return new PageImpl<>(dbData, pageable, total);
    }

    @Override
    public Stream<Station> searchAfter(SearchAfter req) {
        return ddmallWarehouseMapper.searchAfter(req).stream().map(mapper::toDomain);
    }

    // todo JPA 方案
    private Pageable toMySQLPageable(Pageable pageable, Sort defaultSort) {
        // watch out: MySQL injection
        List<Order> orderList = pageable.getSortOr(defaultSort).stream()
            // 可排序的字段配置化
            .filter(this::fieldCanBeUseToSort)
            .map(d -> new Order(d.getDirection(), LOWER_CAMEL.converterTo(LOWER_UNDERSCORE).convert(d.getProperty())))
            .toList();
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orderList));
    }

    private boolean fieldCanBeUseToSort(Order d) {
        boolean ret = configService.getStationRepoMySQLImplSortFields().contains(d.getProperty());
        if (!ret) {
            log.warn("field can not be use to sort: {}", d.getProperty());
        }
        return ret;
    }

    @Mapper
    public interface DomainMapper {
        DomainMapper INSTANCE = Mappers.getMapper(DomainMapper.class);

        List<Station> toDomain(List<DdmallWarehouse> poList);

        DdmallWarehouse toPO(Station station);

        Station toDomain(DdmallWarehouse d);
    }

}
