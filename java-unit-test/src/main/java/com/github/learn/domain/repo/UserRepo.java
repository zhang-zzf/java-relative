package com.github.learn.domain.repo;

import com.github.learn.domain.model.User;
import java.util.Optional;

/**
 * @author zhanfeng.zhang
 * @date 2021/07/04
 */

public interface UserRepo {

    /**
     * getUserByUserId
     *
     * @param userId id
     * @return User
     */
    Optional<User> getById(Long userId);
}
