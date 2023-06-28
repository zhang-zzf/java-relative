package com.feng.learn.redis.lbs.demo.domain.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author zhang.zzf@alibaba-inc.com
 * @date 2021/01/15
 */
@Getter
@Setter
@Accessors(chain = true)
public class Location {

  private Long id;
  /**
   * 纬度
   */
  private Double latitude;
  /**
   * 经度
   */
  private Double longitude;
  /**
   * 端上时间戳
   */
  private LocalDateTime timestamp;
  /**
   * 速度
   */
  private Double speed;
  /**
   * 海拔
   */
  private Double altitude;
  /**
   * 角度
   */
  private Double course;
  private Double hAccuracy;
  private Integer locationType;
  private Boolean noise;
}