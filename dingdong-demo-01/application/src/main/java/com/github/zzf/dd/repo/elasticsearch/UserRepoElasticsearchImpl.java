package com.github.zzf.dd.repo.elasticsearch;

import com.github.zzf.dd.user.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-09-25
 */
@Repository
@Slf4j
@Validated
@Primary
public class UserRepoElasticsearchImpl extends UserRepo.Wrapper implements UserRepo {

    public UserRepoElasticsearchImpl(
            @Qualifier("userRepoRedisImpl") UserRepo delegate) {
        super(delegate);
    }

    @Override
    public List<String> queryByMultiCondition() {
        // 使用 elasticsearch 联合索引搜索
        return super.queryByMultiCondition();
    }
}
