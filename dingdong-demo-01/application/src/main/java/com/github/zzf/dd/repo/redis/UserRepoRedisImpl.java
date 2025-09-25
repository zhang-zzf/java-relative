package com.github.zzf.dd.repo.redis;

import com.github.zzf.dd.common.BatchCacheableRedis;
import com.github.zzf.dd.user.model.User;
import com.github.zzf.dd.user.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-09-25
 */
@Repository
@Slf4j
@Validated
//@Primary
public class UserRepoRedisImpl extends UserRepo.Wrapper implements UserRepo, BatchCacheableRedis<String, User> {

    final @Qualifier(REDIS_TEMPLATE) RedisTemplate<String, User> redisTemplate;

    public UserRepoRedisImpl(
            @Qualifier("userRepoMySQL2MongoDBImpl") UserRepo delegate,
            @Qualifier(REDIS_TEMPLATE) RedisTemplate<String, User> redisTemplate) {
        super(delegate);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<User> queryUserByUserNoList(List<String> userNoList) {
        return batchCacheable(userNoList,
                this::toRedisKey,
                User::queryUserNo,
                ids -> delegate.queryUserByUserNoList(new ArrayList<>(ids))
        );
    }

    private String toRedisKey(String userNo) {
        return "user:" + userNo;
    }

    @Override
    public RedisTemplate<String, User> redisTemplate() {
        return this.redisTemplate;
    }

    public static final String REDIS_TEMPLATE = "RedisTemplate_UserRepoRedisImpl";

    @Configuration
    public static class RedisTemplateAutowire {
        @Bean(REDIS_TEMPLATE)
        public RedisTemplate<String, User> redisTemplate(RedisTemplate redisTemplate) {
            return redisTemplate;
        }
    }

}
