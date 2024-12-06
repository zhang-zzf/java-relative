package com.github.learn.batchcache.domain.repository;

import com.github.learn.batchcache.domain.model.User;
import java.util.List;
import java.util.Set;

/**
 * @author zhanfeng.zhang
 * @date 2020/7/24
 */
public interface UserRepository {

    User insert(User user);

    List<User> batchInsert(Set<User> users);

    void update(User user);

    User getById(Long id);

    List<User> getByIds(Set<Long> ids);

    void deleteByIds(Set<Long> ids);
}
