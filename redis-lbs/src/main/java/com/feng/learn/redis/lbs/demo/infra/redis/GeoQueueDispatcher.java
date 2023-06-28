package com.feng.learn.redis.lbs.demo.infra.redis;

import static java.util.stream.Collectors.toSet;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.util.BoundingBoxGeoHashIterator;
import ch.hsr.geohash.util.TwoGeoHashBoundingBox;
import ch.hsr.geohash.util.VincentyGeodesy;
import com.feng.learn.redis.lbs.demo.domain.model.Location;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2020/08/16
 */
@Service("propertyGeoQueueDispatcher")
@Slf4j
public class GeoQueueDispatcher {

  public static final String METRIC_NAME = GeoQueueDispatcher.class.getSimpleName();

  /**
   * 根据坐标数据把坐标划分到分片
   * <p>特征有： geoHash</p>
   *
   * @param l Location info
   * @return name of the partition
   */
  public String locationToQueue(Location l) {
    if (l == null) {
      return "";
    }
    String geoHash = convertToGeoHash(l.getLatitude(), l.getLongitude());
    String queue = new StringBuilder()
        .append(geoHash)
        .toString();
    return queue;
  }

  private String convertToGeoHash(Double latitude, Double longitude) {
    return GeoHash.withCharacterPrecision(latitude, longitude, 5).toBase32();
  }

  private Set<String> geoHashToSearch(double lat, double lon, long radius) {
    Set<String> geoHashToQuery = new HashSet<>();
    // calculate geoHash to query
    WGS84Point center = new WGS84Point(lat, lon);
    int precision = 5;
    BoundingBox bbox = new BoundingBox(
        VincentyGeodesy.moveInDirection(VincentyGeodesy.moveInDirection(center, 180, radius), 270,
            radius),
        VincentyGeodesy.moveInDirection(VincentyGeodesy.moveInDirection(center, 0, radius), 90,
            radius));
    TwoGeoHashBoundingBox twoGeoHashBoundingBox = TwoGeoHashBoundingBox.withCharacterPrecision(bbox,
        precision);
    BoundingBoxGeoHashIterator it = new BoundingBoxGeoHashIterator(twoGeoHashBoundingBox);
    while (it.hasNext()) {
      geoHashToQuery.add(it.next().toBase32());
    }
    return geoHashToQuery;
  }

  private Set<String> combine(final Set<String> cur, final Set<String> appenders) {
    if (cur == null || cur.isEmpty()) {
      return new HashSet<>(appenders);
    } else if (appenders == null || appenders.isEmpty()) {
      return cur;
    } else if (appenders.size() == 1) {
      String appender = appenders.stream().findFirst().get();
      return cur.stream().map(str -> str.concat(appender)).collect(toSet());
    } else {
      Set<String> ret = new HashSet<>(cur.size() * appenders.size());
      for (String appender : appenders) {
        ret.addAll(cur.stream().map(str -> str.concat(appender)).collect(toSet()));
      }
      return ret;
    }
  }

  public Set<String> searchRequestToQueue(double lat, double lon, long radius) {
    Set<String> geoHashToSearch = geoHashToSearch(lat, lon, radius);
    Set<String> combined = combine(null, geoHashToSearch.stream().collect(toSet()));
    return combined;
  }

}
