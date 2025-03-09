package com.github.zzf.learn.app.repo.mysql.db0.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
  * @author : zhanfeng.zhang@icloud.com
  * @date : 2025-03-09
  */

/**
 * 叮咚买菜前置仓门店信息表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "ddmall_warehouse")
public class DdmallWarehouse {
    /**
     * 前置仓ID，主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "前置仓ID，主键，自增is not null")
    private Long id;

    /**
     * 前置仓名称
     */
    @TableField(value = "`name`")
    @Size(max = 255, message = "前置仓名称max length should less than 255")
    @NotBlank(message = "前置仓名称is not blank")
    private String name;

    /**
     * 前置仓地址
     */
    @TableField(value = "address")
    @Size(max = 255, message = "前置仓地址max length should less than 255")
    @NotBlank(message = "前置仓地址is not blank")
    private String address;

    /**
     * 所在城市
     */
    @TableField(value = "city")
    @Size(max = 100, message = "所在城市max length should less than 100")
    @NotBlank(message = "所在城市is not blank")
    private String city;

    /**
     * 所在区县
     */
    @TableField(value = "district")
    @Size(max = 100, message = "所在区县max length should less than 100")
    @NotBlank(message = "所在区县is not blank")
    private String district;

    /**
     * 联系电话
     */
    @TableField(value = "contact_phone")
    @Size(max = 20, message = "联系电话max length should less than 20")
    private String contactPhone;

    /**
     * 开业时间
     */
    @TableField(value = "open_time")
    private Date openTime;

    /**
     * 营业结束时间
     */
    @TableField(value = "close_time")
    private Date closeTime;

    /**
     * 库存容量
     */
    @TableField(value = "capacity")
    private Integer capacity;

    /**
     * 门店状态
     */
    @TableField(value = "`status`")
    private Object status;

    /**
     * 是否营业
     */
    @TableField(value = "is_operating")
    private Boolean isOperating;

    /**
     * 是否删除
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;

    /**
     * 备注信息
     */
    @TableField(value = "remark")
    @Size(max = 512, message = "备注信息max length should less than 512")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private Date createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at")
    private Date updatedAt;
}