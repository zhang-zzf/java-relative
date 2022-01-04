package com.feng.insure.protocol.insureserver.controller.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 保险中台保单
 *
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/9/23
 */
@Data
@Accessors(chain = true)
public class InsureOrder {

    public static final String BIZ_PARTNER_UID = "partner_uid";
    /**
     * 保司唯一 ID
     */
    private String policyNo;
    /**
     * 保单唯一 ID
     */
    private String insureNo;
    /**
     * 投保人信息
     */
    private List<Insured> insured;
    /**
     * 保险产品
     */
    private InsureProd insureProd;
    /**
     * 保费定价
     */
    private Payment payment;
    /**
     * 被保人信息
     */
    private List<Insurant> insurant;
    /**
     * 保障期限
     */
    private Period period;
    /**
     * 保障范围
     * <p>值对象，无ID</p>
     */
    private List<InsuredItem> insuredItemList;
    /**
     * 附加信息
     * <p>和保单绑定的业务数据</p>
     */
    private Map<String, String> data;
    /**
     * 保司合同信息
     * <p>值对象，无ID</p>
     */
    private InsuranceContract insuranceContract;
    /**
     * 保单创建时间
     */
    private LocalDateTime createdAt;

    @Data
    @Accessors(chain = true)
    public static class Period {

        private LocalDateTime start;
        private LocalDateTime end;

    }

}
