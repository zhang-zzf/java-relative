package com.feng.learn.service;

import com.feng.learn.model.User;

public interface UserService {

  User get(long id);

  boolean save(long id, String name);

  void throwException(boolean flag);
}
