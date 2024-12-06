package com.feng.insure.protocol.insureserver.controller.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 保司合同
 *
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/9/24
 */
@Data
@Accessors(chain = true)
public class InsuranceContract {

    /**
     * 保单 ID
     */
    private String insureNo;
    /**
     * 保司保险合同编号
     */
    private String policyNo;
    /**
     * 附件
     */
    private List<Attachment> attachmentList;
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

}
