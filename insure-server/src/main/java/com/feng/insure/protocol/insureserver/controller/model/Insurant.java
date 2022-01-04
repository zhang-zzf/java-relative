package com.feng.insure.protocol.insureserver.controller.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * <p>被保人</p>
 *
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/9/30
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
public class Insurant extends Person {

    /**
     * 被保人在业务领域的唯一id
     * <p>长度不得超过 64 字节</p>
     * <p>骑手id</p>
     * <p>代理商id</p>
     */
    private String bizId;


    /**
     * 被保人额外信息
     */
    private Map<String, String> extra;

}
