package com.feng.learn.redis.lbs.demo.infra.redis;

import static com.feng.learn.redis.lbs.demo.infra.redis.loc_data.RedisConfig.REDIS_TEMPLATE;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs;

import com.alibaba.fastjson.JSON;
import com.feng.learn.redis.lbs.demo.domain.model.Location;
import com.feng.learn.redis.lbs.demo.domain.repo.LocationRepository;
import com.feng.learn.redis.lbs.demo.infra.redis.loc_data.RedisConfig;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author zhanfeng.zhang
 * @date 2020/12/11
 */
@Repository
@Slf4j
public class LocationRepositoryRedisImpl implements LocationRepository {

    private static final String LOCATION_GEO_QUEUE_PREFIX = RedisConfig.LOCATION_GEO_QUEUE_PREFIX;
    private static final String LOCATION_DATA_PREFIX = RedisConfig.LOCATION_DATA_PREFIX;
    final RedisTemplate redisTemplate;
    final GeoQueueDispatcher geoQueueDispatcher;
    final GeoRadiusCommandArgs GEO_RADIUS_COMMAND_ARGS = newGeoRadiusArgs()
        //.includeCoordinates().includeDistance()
        .includeDistance()
        .sortAscending();
    private final String METRIC_NAME = getClass().getSimpleName();

    public LocationRepositoryRedisImpl(
        @Qualifier(REDIS_TEMPLATE) RedisTemplate redisTemplate,
        GeoQueueDispatcher geoQueueDispatcher) {
        this.redisTemplate = redisTemplate;
        this.geoQueueDispatcher = geoQueueDispatcher;
    }

    @Override
    public void updateLocation(List<Location> locations) {
        if (locations.isEmpty()) {
            return;
        }
        List<Location> curLocations = batchGetById(
            locations.stream().map(Location::getId).collect(toList()));
        // 更新 Geo Index
        updateGeoIndex(locations, curLocations);
        // 更新坐标
        updateLocationCache(locations);
    }

    private void updateGeoIndex(List<Location> locations, List<Location> curData) {
        updateGeoIndexUsePipeline(locations);
        removeFromGeoForQueueChangedLocation(locations, curData);
    }

    private void updateGeoIndexUsePipeline(List<Location> locations) {
        Map<String, List<Location>> keys2Data = locations.stream()
            .collect(groupingBy(this::locationToQueue, toList()));
        Map<String, Map<byte[], Point>> queue2MemberAndPoint = new HashMap<>(keys2Data.size());
        for (Entry<String, List<Location>> entry : keys2Data.entrySet()) {
            Map<byte[], Point> memberAndPoint = entry.getValue().stream().collect(toMap(
                l -> rawValue(idOfLocation(l)),
                l -> new Point(l.getLongitude(), l.getLatitude()),
                (o1, o2) -> o2
            ));
            queue2MemberAndPoint.putIfAbsent(entry.getKey(), memberAndPoint);
        }
        redisTemplate.executePipelined((RedisCallback<Void>) connection -> {
            for (Entry<String, Map<byte[], Point>> entry : queue2MemberAndPoint.entrySet()) {
                connection.geoAdd(rawKey(entry.getKey()), entry.getValue());
            }
            return null;
        });
    }

    private String locationToQueue(Location loc) {
        return LOCATION_GEO_QUEUE_PREFIX + geoQueueDispatcher.locationToQueue(loc);
    }

    private Set<String> searchRequestToQueue(double lat, double lon, long radius) {
        Set<String> queues = geoQueueDispatcher.searchRequestToQueue(lat, lon, radius);
        return queues.stream().map(str -> LOCATION_GEO_QUEUE_PREFIX + str).collect(Collectors.toSet());
    }

    private void removeFromGeoForQueueChangedLocation(List<Location> locations,
        List<Location> curData) {
        Map<Long, Location> id2Location = locations.stream()
            .collect(toMap(Location::getId, Function.identity()));
        Map<String, List<Location>> queue2Data = curData.stream()
            // 坐标变换了队列
            .filter(d -> !locationToQueue(id2Location.get(d.getId())).equals(locationToQueue(d)))
            .collect(groupingBy(this::locationToQueue, toList()));
        if (queue2Data.isEmpty()) {
            return;
        }
        redisTemplate.executePipelined(new RedisCallback<Void>() {
            @Override
            public Void doInRedis(RedisConnection connection) throws DataAccessException {
                for (Entry<String, List<Location>> entry : queue2Data.entrySet()) {
                    List<Location> values = entry.getValue();
                    byte[][] members = new byte[values.size()][];
                    int i = 0;
                    for (Location v : values) {
                        members[i++] = redisTemplate.getValueSerializer().serialize(idOfLocation(v));
                    }
                    connection.zRem(redisTemplate.getKeySerializer().serialize(entry.getKey()), members);
                }
                return null;
            }
        });
    }

    private String idOfLocation(Location v) {
        return v.getId().toString();
    }

    private void updateLocationCache(List<Location> locations) {
        redisTemplate.executePipelined((RedisCallback<Void>) connection -> {
            for (Location l : locations) {
                connection.set(rawKey(idToDataKey(l.getId())), rawValue(JSON.toJSONString(l)));
            }
            return null;
        });
    }

    @Override
    public List<Location> batchGetById(List<Long> ids) {
        List<String> list = redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            for (Long id : ids) {
                connection.get(rawKey(idToDataKey(id)));
            }
            return null;
        });
        return list.stream()
            .filter(Objects::nonNull)
            .map(d -> JSON.parseObject(d, Location.class))
            .collect(toList());
    }

    @Override
    public List<Long> searchGeo(double lat, double lon, long radius) {
        Set<String> queues = searchRequestToQueue(lat, lon, radius);
        List<GeoResults<GeoLocation<byte[]>>> geoResults = pipelineSearchGeo(queues,
            new Circle(lon, lat, radius));
        List<Long> ids = geoResults.stream()
            .map(GeoResults::getContent)
            .flatMap(List::stream)
            .sorted(Comparator.comparingDouble(r -> r.getDistance().getValue()))
            .map(r -> r.getContent().getName())
            .map(bytes -> (String) redisTemplate.getValueSerializer().deserialize(bytes))
            .map(Long::valueOf)
            .collect(toList());
        logTraceAndMetric(lat, lon, radius, queues, ids);
        return ids;
    }

    private void logTraceAndMetric(double lat, double lon, long radius, Set<String> queues,
        List<Long> ids) {
        try {
            StringBuilder logStr = new StringBuilder()
                .append("lat").append("=").append(lat)
                .append(',').append("lon").append('=').append(lon)
                .append(',').append("radius").append('=').append(radius)
                .append(',').append("queueSize").append('=').append(queues.size())
                .append(',').append("retSize").append('=').append(ids.size());
            log.info("data=>{}, searchQueues=>{}", logStr, queues);
        } catch (Exception e) {
            // ignore
            log.error("unExpect Error:", e);
        }
    }

    private List<GeoResults<GeoLocation<byte[]>>> pipelineSearchGeo(Set<String> queues,
        Circle within) {
        return redisTemplate.executePipelined(
            (RedisCallback<GeoResults<GeoLocation<byte[]>>>) connection -> {
                for (String queue : queues) {
                    connection.geoRadius(rawKey(queue), within, GEO_RADIUS_COMMAND_ARGS);
                }
                return null;
            });
    }

    private byte[] rawKey(Object key) {
        return redisTemplate.getKeySerializer().serialize(key);
    }

    private byte[] rawValue(Object value) {
        return redisTemplate.getHashValueSerializer().serialize(value);
    }

    private String idToDataKey(long id) {
        return LOCATION_DATA_PREFIX + id;
    }

}

