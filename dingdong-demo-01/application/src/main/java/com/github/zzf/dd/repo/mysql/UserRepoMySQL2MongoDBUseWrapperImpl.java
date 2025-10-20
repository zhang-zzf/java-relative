package com.github.zzf.dd.repo.mysql;


import com.github.zzf.dd.common.ConfigService;
import com.github.zzf.dd.config.spring.async.SpringAsyncConfig;
import com.github.zzf.dd.repo.mongo.UserRepoMongoDBImpl;
import com.github.zzf.dd.user.model.User;
import com.github.zzf.dd.user.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.util.concurrent.Executor;

@Repository
@Slf4j
@Validated
//@Primary
public class UserRepoMySQL2MongoDBUseWrapperImpl extends UserRepo.Wrapper implements UserRepo, Migrate {

    final UserRepoMySQLImpl v1;
    final UserRepoMongoDBImpl v2;
    final ConfigService config;
    final Executor executor;

    public UserRepoMySQL2MongoDBUseWrapperImpl(
            UserRepoMySQLImpl v1,
            UserRepoMongoDBImpl v2,
            ConfigService config,
            @Qualifier(SpringAsyncConfig.ASYNC_THREAD) Executor executor) {
        super(v1);
        this.v1 = v1;
        this.v2 = v2;
        this.config = config;
        this.executor = executor;
    }

    @Override
    public User queryUserByUserNo(String userNo) {
        return migrate(
                () -> v1.queryUserByUserNo(userNo),
                () -> v2.queryUserByUserNo(userNo),
                config.switchOn("UserRepoMySQL2MongoDBImpl.queryUserByUserNo.v2", false),
                config.switchOn("UserRepoMySQL2MongoDBImpl.queryUserByUserNo.check", false),
                executor
        );
    }

}
