package com.feng.insure.protocol.insureserver.controller.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 保险费定价
 *
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/10/28
 */
@Data
@Accessors(chain = true)
public class Payment {

  /**
   * 保费
   * <p>需要向保险公司支付的实际费用</p>
   * <p>单位：分</p>
   */
  private Long premium;
  /**
   * 定价细节
   */
  private Map<String, Long> detail;

  /**
   * 添加定价细节
   *
   * @param key   定价项目
   * @param value 值
   * @return this
   */
  public Payment addDetail(String key, Long value) {
    if (detail == null) {
      detail = new HashMap<>();
    }
    detail.put(key, value);
    return this;
  }


}
