package com.github.zzf.learn.app.repo;

import static com.github.zzf.learn.app.repo.StationRepoMySQLImpl.BEAN_NAME;
import static com.github.zzf.learn.app.repo.StationRepoMySQLImpl.DomainMapper.INSTANCE;

import com.github.zzf.learn.app.repo.mysql.db0.entity.DdmallWarehouse;
import com.github.zzf.learn.app.repo.mysql.db0.mapper.DdmallWarehouseMapper;
import com.github.zzf.learn.app.station.model.Station;
import com.github.zzf.learn.app.station.model.StationIdList;
import com.github.zzf.learn.app.station.repo.StationRepo;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
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
            List<DdmallWarehouse> dbData = ddmallWarehouseMapper.selectByIdList(it.next());
            ret.addAll(mapper.toDomain(dbData));
        }
        return ret;
    }

    @Override
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


    @Mapper
    public interface DomainMapper {
        DomainMapper INSTANCE = Mappers.getMapper(DomainMapper.class);

        List<Station> toDomain(List<DdmallWarehouse> poList);

        DdmallWarehouse toPO(Station station);
    }

}
