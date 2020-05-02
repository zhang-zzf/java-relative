package com.feng.learn.service;

import com.feng.learn.exception.ServiceException;


public interface ServiceA {

    int service(int param) throws ServiceException;

    /**
     * 创建用户
     *
     * @return 用户的id
     */
    long createUser(String name, int age);

}
