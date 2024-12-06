package com.feng.insure.protocol.insureserver.controller.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 保险公司模型
 *
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/9/23
 */
@Data
@Accessors(chain = true)
public class InsuranceCarrier {

    /**
     * 保司提供的保险产品信息
     */
    @Data
    @Accessors(chain = true)
    public static class Prod {

        /**
         * 产品id
         */
        private String prodCode;

    }


}
