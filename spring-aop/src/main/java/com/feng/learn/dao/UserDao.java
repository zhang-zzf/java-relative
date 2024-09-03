package com.feng.learn.dao;

import com.feng.learn.model.User;

public interface UserDao {
  User get(long id);

  boolean save(long id, String name);
}
