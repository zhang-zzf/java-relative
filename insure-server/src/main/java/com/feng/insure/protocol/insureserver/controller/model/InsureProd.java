package com.feng.insure.protocol.insureserver.controller.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 保险产品
 * <p>配置类</p>
 *
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/9/23
 */
@Data
@Accessors(chain = true)
public class InsureProd {

  /**
   * 保司保险产品
   * <p>保险中台不提供保险产品，仅封装第三方服务机构的产品</p>
   * <p>值对象</p>
   */
  private InsuranceCarrier.Prod insuranceCarrierProd;

}
