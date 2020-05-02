package com.feng.learn.dao;

import com.feng.learn.dao.model.User;

public interface UserDao {

    User getById(long id);

    int createUser(User user);

    int updateUser(User user);

}
