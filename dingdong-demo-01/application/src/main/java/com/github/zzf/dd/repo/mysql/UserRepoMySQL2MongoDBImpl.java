package com.github.zzf.dd.repo.mysql;


import com.github.zzf.dd.common.ConfigService;
import com.github.zzf.dd.config.spring.async.SpringAsyncConfig;
import com.github.zzf.dd.repo.mongo.UserRepoMongoDBImpl;
import com.github.zzf.dd.user.model.User;
import com.github.zzf.dd.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.concurrent.Executor;

@Repository
@Slf4j
@RequiredArgsConstructor
@Validated
//@Primary
public class UserRepoMySQL2MongoDBImpl implements UserRepo, Migrate {

    final UserRepoMySQLImpl v1;
    final UserRepoMongoDBImpl v2;
    final ConfigService config;
    @Qualifier(SpringAsyncConfig.ASYNC_THREAD)
    final Executor executor;

    @Override
    public List<User> queryUserByUserNoList(List<String> userNoList) {
        return List.of();
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

    @Override
    public long tryCreateUser(User user) {
        long ret = v1.tryCreateUser(user);
        v2.tryCreateUser(user);
        return ret;
    }

    @Override
    public List<String> queryByMultiCondition() {
        return List.of();
    }

}
