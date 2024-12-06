package com.github.learn.batchcache.infrastruture.repo;

import static com.github.learn.batchcache.infrastruture.spring.async.ThreadPoolForRedisCache.THREAD_POOL_NAME;
import static com.github.learn.batchcache.infrastruture.spring.cache.CacheConfig.REDIS_CACHE_MANAGER;
import static com.github.learn.batchcache.infrastruture.spring.cache.CacheConfig.REDIS_CACHE_NAME_1;
import static java.util.stream.Collectors.toList;

import com.github.learn.batchcache.domain.model.User;
import com.github.learn.batchcache.domain.repository.UserRepository;
import com.github.learn.batchcache.infrastruture.mysql.UserMapper;
import com.github.learn.batchcache.infrastruture.spring.cache.CacheNotFoundException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

/**
 * @author zhanfeng.zhang
 * @date 2020/7/24
 */
@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;
    private final UserMapperCache userMapperCache;
    private final AsyncTaskExecutor asyncTaskExecutor;

    public UserRepositoryImpl(UserMapper userMapper,
        UserMapperCache userMapperCache,
        @Qualifier(THREAD_POOL_NAME) AsyncTaskExecutor asyncTaskExecutor) {
        this.userMapper = userMapper;
        this.userMapperCache = userMapperCache;
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    @Override
    public User insert(User user) {
        userMapper.insert(user);
        userMapperCache.asyncDeleteCache(user.getId());
        return user;
    }

    @Override
    public List<User> batchInsert(Set<User> users) {
        userMapper.batchInsert(users);
        for (User user : users) {
            userMapperCache.asyncDeleteCache(user.getId());
        }
        return Lists.newArrayList(users);
    }

    @Override
    public void update(User user) {
        userMapper.updateById(user);
        userMapperCache.asyncDeleteCache(user.getId());

    }

    @Override
    public User getById(Long id) {
        return userMapperCache.getById(id);
    }

    public List<User> getByIds2(Set<Long> ids) {
        // 线程池批量读取
        List<Future<User>> futures = new ArrayList<>(ids.size());
        for (Long id : ids) {
            // 存在风险，瞬间压垮 DB
            futures.add(asyncTaskExecutor.submit(() -> userMapperCache.getById(id)));
        }
        return futures.stream()
            .map(this::getDataFromFuture)
            .filter(Objects::nonNull)
            .collect(toList());
    }

    @Override
    public List<User> getByIds(Set<Long> ids) {
        // 线程池批量异步读取
        List<Future<User>> futures = new ArrayList<>(ids.size());
        for (Long id : ids) {
            futures.add(asyncTaskExecutor.submit(() -> getUserFromCache(id)));
        }
        List<User> caches = futures.stream().map(this::getDataFromFuture).filter(Objects::nonNull)
            .collect(toList());
        // 全部命中缓存
        if (caches.size() == ids.size()) {
            return caches;
        }
        // 一次 DB query
        List<User> dbData = userMapper.getByIds(ids);
        // 放入 cache 全部刷新一遍缓存
        dbData.forEach(user -> asyncTaskExecutor.submit(() -> userMapperCache.addToCache(user)));
        return dbData;
    }

    private User getUserFromCache(Long id) {
        try {
            return userMapperCache.getFromCache(id);
        } catch (CacheNotFoundException e) {
            return null;
        }
    }

    private <T> T getDataFromFuture(Future<T> future) {
        try {
            return future.get();
        } catch (Exception e) {
            log.error("future.get() exception.", e);
            return null;
        }
    }

    @Override
    public void deleteByIds(Set<Long> ids) {
        // 批量删除 DB
        userMapper.deleteByIds(ids);
        ids.forEach(id -> userMapperCache.asyncDeleteCache(id));
    }

    @Repository
    @RequiredArgsConstructor
    @CacheConfig(cacheNames = {REDIS_CACHE_NAME_1}, cacheManager = REDIS_CACHE_MANAGER)
    static class UserMapperCache {

        private final UserMapper userMapper;

        @Cacheable(key = "'/u/' + #root.args[0]")
        public User getById(Long id) {
            return userMapper.getByIds(Sets.newHashSet(id)).stream().findAny().orElse(null);
        }

        /**
         * 删除 cache
         *
         * @param id id
         */
        @CacheEvict(key = "'/u/' + #root.args[0]")
        @Async(THREAD_POOL_NAME)
        public void asyncDeleteCache(Long id) {
        }

        @Cacheable(key = "'/u/' + #root.args[0]")
        public User getFromCache(Long id) throws CacheNotFoundException {
            // read from cache -> hit cache -> return cache
            // read from cache -> not hit cache -> throw Exception to interrupt
            throw new CacheNotFoundException();
        }

        @CachePut(key = "'/u/' + #root.args[0].id")
        public User addToCache(User user) {
            return user;
        }

    }
}
