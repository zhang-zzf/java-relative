create table tb_user
(
    id            bigint unsigned not null auto_increment,
    type          varchar(8)      not null comment '`card/mobile` 卡用户 / 手机登陆用户',
    username      varchar(32)     not null comment '卡号/手机号',
    password      varchar(128)    not null comment '密码',
    user_no       varchar(48)     not null comment '用户唯一ID = type/username',
    admin_user_no varchar(48) comment '用户归属的管理员ID',
    is_admin      tinyint         not null default 0 comment '是否为管理员账号。1-是',
    created_at    datetime        not null default current_timestamp comment '创建时间',
    updated_at    datetime        not null default current_timestamp on update current_timestamp comment '创建时间',
    primary key (id),
    unique key uk_user_name (username), -- 卡号和手机号不能重复
    key idx_user_no (user_no)
) comment '用户表';
