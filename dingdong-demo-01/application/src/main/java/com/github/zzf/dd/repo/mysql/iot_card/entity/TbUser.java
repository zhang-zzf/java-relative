package com.github.zzf.dd.repo.mysql.iot_card.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-11-11
 */
@Getter
@Setter
@TableName("tb_user")
public class TbUser {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * `card/mobile` 卡用户 / 手机登陆用户
     */
    @TableField("type")
    private String type;

    /**
     * 卡号/手机号
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 用户唯一ID = type/username
     */
    @TableField("user_no")
    private String userNo;

    /**
     * 用户归属的管理员ID
     */
    @TableField("admin_user_no")
    private String adminUserNo;

    /**
     * 是否为管理员账号。1-是
     */
    @TableField("is_admin")
    private Boolean isAdmin;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 创建时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
