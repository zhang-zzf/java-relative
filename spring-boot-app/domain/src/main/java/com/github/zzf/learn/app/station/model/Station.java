package com.github.zzf.learn.app.station.model;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-03-08
 */
@Getter
@Builder
public class Station {

    /**
     * 前置仓ID，主键，自增
     */
    private Long id;

    /**
     * 前置仓名称
     */
    private String name;

    /**
     * 前置仓地址
     */
    private String address;

    /**
     * 所在城市
     */
    private String city;

    /**
     * 所在区县
     */
    private String district;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 开业时间
     */
    private Date openTime;

    /**
     * 营业结束时间
     */
    private Date closeTime;

    /**
     * 库存容量
     */
    private Integer capacity;

    /**
     * 门店状态
     */
    private Object status;

    /**
     * 是否营业
     */
    private Boolean isOperating;

    /**
     * 是否删除
     */
    private Boolean isDeleted;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

}
