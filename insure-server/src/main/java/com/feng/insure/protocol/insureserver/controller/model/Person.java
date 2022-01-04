package com.feng.insure.protocol.insureserver.controller.model;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>保险领域人模型</p>
 * <p>自然人/法人</p>
 *
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/9/30
 */
@Data
@Accessors(chain = true)
public class Person {

    public static final String TYPE_ID = "01";

    /**
     * 证件类型
     * <p>约定：0<个人的类型<100</p>
     * <p>约定：1000<团体的类型<10000</p>
     * <p>个人(01：身份证、02：护照、03：军人证、05：驾驶证、99：其他)</p>
     * <p>团体(1001：组织机构代码证、1002：税务登记证、1003：统一社会信用代码、1004：工商营业执照号、1099：其他)</p>
     */
    private String certType;
    /**
     * 证件号码
     */
    private String certNo;
    /**
     * 名字
     */
    private String certName;
    /**
     * 手机号
     */
    private String mobile;

}
