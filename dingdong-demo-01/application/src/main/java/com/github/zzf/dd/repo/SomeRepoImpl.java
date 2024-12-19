package com.github.zzf.dd.repo;

import static com.github.zzf.dd.common.spring.async.ThreadPoolForRedisCache.ASYNC_THREAD;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.empty;

import com.github.zzf.dd.redis_multi_get.repo.SomeRepo;
import com.github.zzf.dd.user.model.User;
import com.github.zzf.dd.user.model.User.Address;
import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2024-12-10
 */
@Repository("someRepoImpl")
@RequiredArgsConstructor
@Slf4j
public class SomeRepoImpl implements SomeRepo {

    public static final int BATCH_SIZE = 4;

    final @Qualifier(ASYNC_THREAD) Executor executor;

    @Override
    public User getBy(String area, String userNo) {
        return User.from(userNo).setCreatedAt(LocalDateTime.now()).setAddress(new Address(area));
    }

    @Override
    public List<User> getBy(String area, List<String> userNoList) {
        return Lists.partition(userNoList, BATCH_SIZE).stream()
            // async get from cache
            .map(list -> supplyAsync(() -> fetchFromDb(area, list), executor))
            // combine all the future task
            .reduce(completedFuture(empty()), (a, b) -> a.thenCombine(b, Stream::concat))
            // wait for task done
            .join()
            .collect(toList());

    }

    private Stream<User> fetchFromDb(String area, List<String> userNoList) {
        log.info("fetchFromDb -> userNoList: {}", userNoList);
        return userNoList.stream().map(User::from);
    }

}
