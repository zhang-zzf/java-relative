package com.feng.insure.protocol.insureserver.controller.model;

import static java.util.Optional.ofNullable;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 理赔信息
 *
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/9/24
 */
@Data
@Accessors(chain = true)
public class Claim {

  /**
   * 理赔唯一 ID
   */
  private String claimNo;
  /**
   * 报案人
   * <p>被保人</p>
   */
  private Insurant reporter;
  /**
   * 事故描述
   */
  private Accident accident;
  /**
   * 保单
   * <p>理赔不一定和保单绑定，有些场景下无保单也可申请理赔：职业伤害险无保单理赔</p>
   */
  private InsureOrder insureOrder;
  /**
   * 保司报案号
   */
  private String carrierReportNo;
  /**
   * 理赔发起时间
   */
  private LocalDateTime createdAt = LocalDateTime.now();
  /**
   * 理赔过程数据
   */
  private List<Transaction> transactionList;

  /**
   * 获取阶段最新的事务数据
   *
   * @param phase 阶段
   * @return 数据
   */
  public Optional<Transaction> getLatestTransaction(String phase) {
    final Optional<Transaction> ret = getTransactionList().stream()
        .filter(t -> phase.equals(t.getPhaseId()))
        .sorted(Comparator.comparing(Transaction::getCreatedAt))
        .findFirst();
    return ret;
  }

  /**
   * 理赔过程中的一次事务交互
   * <p>报案</p>
   * <p>提交材料</p>
   * <p>保司结果回传</p>
   * <p>补充材料</p>
   * <p>理赔结果数据</p>
   */
  @Data
  @Accessors(chain = true)
  public static class Transaction {

    /**
     * 阶段id
     */
    private String phaseId;

    /**
     * 附件
     */
    private List<Attachment> attachmentList;
    /**
     * 额外数据
     */
    private Map<String, String> data;
    /**
     * 发生时间
     */
    private LocalDateTime createdAt = LocalDateTime.now();

  }

  /**
   * 报案事故信息
   */
  @Data
  @Accessors(chain = true)
  public static class Accident {

    /**
     * 发生时间
     */
    private LocalDateTime happenedAt;
    /**
     * 事故发生地点
     */
    private String province;
    private String provinceId;
    private String city;
    private String cityId;
    private String district;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 事故详细描述
     */
    private String detail;
    /**
     * 事故额外信息
     */
    private Map<String, String> data;

    public String queryData(String key) {
      return ofNullable(data).map(d -> d.get(key)).orElse(null);
    }

  }

}
