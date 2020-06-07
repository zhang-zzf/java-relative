package com.feng.learn.spring.aop.service;

import com.feng.learn.dao.model.User;

/**
 * @author zhanfeng.zhang
 * @date 2020/6/6
 */
public interface UserService {

    User get(long id);

    boolean save(long id, String name);

    void throwException(boolean b);
}
