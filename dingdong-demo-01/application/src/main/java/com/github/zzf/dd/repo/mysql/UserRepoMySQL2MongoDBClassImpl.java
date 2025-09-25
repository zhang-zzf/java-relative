package com.github.zzf.dd.repo.mysql;


import com.github.zzf.dd.common.ConfigService;
import com.github.zzf.dd.config.spring.async.SpringAsyncConfig;
import com.github.zzf.dd.repo.mongo.UserRepoMongoDBImpl;
import com.github.zzf.dd.repo.mysql.iot_card.mapper.TbUserMapper;
import com.github.zzf.dd.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;

import java.util.concurrent.Executor;

/**
 * <pre>
 * 继承 MySQL 实现，
 * 优势: 仅需 override 需要迁移的接口
 * 劣势：父类的构造器依赖
 * </pre>
 */
//@Repository
@Slf4j
@Validated
public class UserRepoMySQL2MongoDBClassImpl extends UserRepoMySQLImpl implements Migrate {

    final UserRepoMongoDBImpl v2;
    final ConfigService config;
    final Executor executor;

    public UserRepoMySQL2MongoDBClassImpl(
            TbUserMapper tbUserMapper,
            UserRepoMongoDBImpl v2,
            ConfigService config,
            @Qualifier(SpringAsyncConfig.ASYNC_THREAD) Executor executor) {
        super(tbUserMapper);
        this.v2 = v2;
        this.config = config;
        this.executor = executor;
    }

    @Override
    public User queryUserByUserNo(String userNo) {
        return migrate(
                () -> super.queryUserByUserNo(userNo),
                () -> v2.queryUserByUserNo(userNo),
                config.querySwitchOn("UserRepoMySQL2MongoDBImpl.queryUserByUserNo.v2", false),
                config.querySwitchOn("UserRepoMySQL2MongoDBImpl.queryUserByUserNo.check", false),
                executor
        );
    }

}
