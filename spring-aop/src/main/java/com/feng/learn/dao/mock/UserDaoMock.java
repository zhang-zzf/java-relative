package com.feng.learn.dao.mock;

import com.feng.learn.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDaoMock implements com.feng.learn.dao.UserDao {
    @Override
    public User get(long id) {
        User user = new User();
        user.setId(1);
        user.setName("feng");
        return user;
    }

    @Override
    public boolean save(long id, String name) {
        return true;
    }
}
