CREATE TABLE ddmall_warehouse
(
    id            INT AUTO_INCREMENT PRIMARY KEY COMMENT '前置仓ID，主键，自增',                                         -- 前置仓ID，主键，自增
    name          VARCHAR(255) NOT NULL COMMENT '前置仓名称',                                                          -- 前置仓名称
    address       VARCHAR(255) NOT NULL COMMENT '前置仓地址',                                                          -- 前置仓地址
    city          VARCHAR(100) NOT NULL COMMENT '所在城市',                                                            -- 所在城市
    district      VARCHAR(100) NOT NULL COMMENT '所在区县',                                                            -- 所在区县
    contact_phone VARCHAR(20) COMMENT '联系电话',                                                                      -- 联系电话
    open_time     TIME COMMENT '开业时间',                                                                             -- 开业时间
    close_time    TIME COMMENT '营业结束时间',                                                                         -- 营业结束时间
    capacity      INT COMMENT '库存容量',                                                                              -- 库存容量
    status        ENUM ('active', 'inactive') DEFAULT 'active' COMMENT '门店状态',                                     -- 门店状态
    is_operating  BOOLEAN                     DEFAULT TRUE COMMENT '是否营业',                                         -- 是否营业
    is_deleted    BOOLEAN                     DEFAULT FALSE COMMENT '是否删除',                                        -- 是否删除
    remark        VARCHAR(512) COMMENT '备注信息',                                                                     -- 备注信息
    created_at    TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',                            -- 创建时间
    updated_at    TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' -- 更新时间
) COMMENT ='叮咚买菜前置仓门店信息表';

