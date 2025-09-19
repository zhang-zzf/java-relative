package com.github.zzf.dd.repo.mysql;


import com.github.zzf.dd.common.ConfigService;
import com.github.zzf.dd.config.spring.async.SpringAsyncConfig;
import com.github.zzf.dd.repo.mongo.UserRepoMongoDBImpl;
import com.github.zzf.dd.user.model.User;
import com.github.zzf.dd.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.sql.Array;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.zzf.dd.utils.MetricsUtil.logEvent;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Repository
@Slf4j
@RequiredArgsConstructor
@Validated
@Primary
public class UserRepoMySQL2MongoDBImpl implements UserRepo {

    final UserRepoMySQLImpl v1;
    final UserRepoMongoDBImpl v2;
    final ConfigService config;
    @Qualifier(SpringAsyncConfig.ASYNC_THREAD)
    final Executor executor;

    @Override
    public User queryUserByUserNo(String userNo) {
        return queryUserByUserNoCheck(
                () -> List.of(v1.queryUserByUserNo(userNo)),
                () -> List.of(v2.queryUserByUserNo(userNo)),
                config.querySwitchOn("UserRepoMySQL2MongoDBImpl.queryUserByUserNo.v2", false),
                config.querySwitchOn("UserRepoMySQL2MongoDBImpl.queryUserByUserNo.check", false),
                executor,
                User::getUserNo
        ).findAny().orElse(null);
    }

    private <T> Stream<T> queryUserByUserNoCheck(
            Supplier<List<T>> v1Func,
            Supplier<List<T>> v2Func,
            boolean useV2,
            boolean check,
            Executor executor,
            Function<T, String> identifier
    ) {
        if (!useV2) {
            List<T> v1Data = v1Func.get();
            if (check) {
                doCheck(new ArrayList<>(v1Data), null, v1Func, v2Func, executor, identifier);
            }
            return v1Data.stream();
        } else {
            List<T> v2Data = v2Func.get();
            if (check) {
                doCheck(null, new ArrayList<>(v2Data), v1Func, v2Func, executor, identifier);
            }
            return v2Data.stream();
        }
    }

    private <T> void doCheck(
            List<T> v1,
            List<T> v2,
            Supplier<List<T>> v1Func,
            Supplier<List<T>> v2Func,
            Executor executor,
            Function<T,String> identifier) {
        Runnable task = () -> {
            List<T> v1Data = ofNullable(v1).orElseGet(v1Func);
            List<T> v2Data = ofNullable(v2).orElseGet(v2Func);
            if (v1Data.size() != v2Data.size()) {
                // todo logEvent
            }
            Map<String, T> v2Map = v2Data.stream().collect(toMap(identifier, d -> d));
            for (T v1Datum : v1Data) {
                T v2Datum = v2Map.get(identifier.apply(v1Datum));
                if (!Objects.equals(v1Datum, v2Datum)) {
                    // todo logEvent
                }
            }
        };
        if (executor == null) {
            task.run();
        } else {
            executor.execute(task);
        }
    }

    private <T> void doCheckObject(T v1Data, T v2Data) {

    }

    private <T> void doCheckList(List<Object> v1Data, List v2Data) {

    }

    @Override
    public long tryCreateUser(User user) {
        return 0;
    }
}
