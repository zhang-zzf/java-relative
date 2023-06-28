package com.feng.insure.protocol.insureserver.controller.model;

import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 投保人模型
 * <p>保险合同的发起人，保费的支付人</p>
 *
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/9/23
 */
@Data
@Accessors(chain = true)
public class Insured extends Person {

  /**
   * 投保人在业务领域的唯一id
   * <p>长度不得超过 64 字节</p>
   * <p>代理商的 agencyId</p>
   * <p>商户的 id</p>
   */
  private String bizId;

  /**
   * 被保人额外信息
   */
  private Map<String, String> extra;

  /**
   * 投保人id生成算法
   *
   * @return id
   */
  public String getId() {
    return getCertType() + ":" + getCertNo() + ":" + getBizId() == null ? "" : getBizId();
  }

}
