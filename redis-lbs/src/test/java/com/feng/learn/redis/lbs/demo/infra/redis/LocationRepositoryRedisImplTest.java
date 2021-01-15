package com.feng.learn.redis.lbs.demo.infra.redis;

import com.feng.learn.redis.lbs.demo.domain.model.Location;
import com.feng.learn.redis.lbs.demo.domain.repo.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/15
 */
@SpringBootTest
@Slf4j
class LocationRepositoryRedisImplTest {

    @Autowired
    LocationRepository repo;

    @Test
    void updateLocation() {
        // given
        LocalDateTime now = LocalDateTime.now();
        // 121.395985,31.230412 wtw3d
        Location loc1 = new Location().setId(1L).setLatitude(31.230412)
            .setLongitude(121.395985).setTimestamp(now.minusSeconds(50));
        // 121.42903,31.225495 wtw3e
        Location loc2 = new Location().setId(2L).setLatitude(31.225495)
            .setLongitude(121.42903).setTimestamp(now);
        repo.updateLocation(Lists.newArrayList(loc1, loc2));
        // when
        List<Location> locations = repo.batchGetById(Lists.newArrayList(1L, 2L));
        // then
        then(locations).hasSize(2);
        // when
        List<Long> ids = repo.searchGeo(31.230412, 121.395985, 5000);
        then(ids).hasSize(2).containsExactly(1L, 2L);
    }

}
