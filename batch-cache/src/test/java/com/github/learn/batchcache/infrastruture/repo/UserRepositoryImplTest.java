package com.github.learn.batchcache.infrastruture.repo;

import com.github.learn.batchcache.domain.model.User;
import com.github.learn.batchcache.domain.repository.UserRepository;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.from;
import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author zhanfeng.zhang
 * @date 2020/7/25
 */
@SpringBootTest
class UserRepositoryImplTest {

    @Autowired UserRepository userRepository;

    @Test
    void insert() {
        userRepository.insert(new User().setName("zhanfeng.zhang"));
    }

    @Test
    @Disabled
    void batchInsert() throws InterruptedException {
        userRepository.batchInsert(Sets.newHashSet(new User(), new User().setName("")));
    }

    @Test
    void update() {
    }

    @Test
    void getById() {
        User user = userRepository.getById(1L);
        then(user).isNotNull()
            .returns(1L, from(User::getId))
            .returns("1", from(User::getName));
        // 再次请求看是否命中缓存
        user = userRepository.getById(1L);
        then(user).isNotNull()
            .returns(1L, from(User::getId))
            .returns("1", from(User::getName));
    }

    @Test
    void getByIds() {
        userRepository.getByIds(Sets.newHashSet(1L, 2L, 3L, 4L));
        userRepository.getByIds(Sets.newHashSet(1L, 2L, 3L, 4L));
        userRepository.getByIds(Sets.newHashSet(1L, 2L));
    }

    @Test
    void deleteByIds() {
    }
}