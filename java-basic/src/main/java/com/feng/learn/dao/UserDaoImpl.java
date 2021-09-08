package com.feng.learn.dao;

import com.feng.learn.dao.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.NonNull;

/**
 * 模拟db操作
 */
// @Repository
public class UserDaoImpl implements UserDao {

    private final List<User> db = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger();

    @Override
    public User getById(long id) {
        Optional<User> any = db.stream().filter(u -> u.getId() == id).findAny();
        return any.isPresent() ? any.get() : null;
    }

    @Override
    public int createUser(@NonNull User user) {
        user.setId(idGenerator.getAndIncrement());
        db.add(user);
        return 1;
    }

    @Override
    public int updateUser(User user) {
        return 0;
    }
}
