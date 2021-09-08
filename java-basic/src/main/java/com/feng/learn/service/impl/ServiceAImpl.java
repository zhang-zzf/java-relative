package com.feng.learn.service.impl;

import com.feng.learn.dao.UserDao;
import com.feng.learn.dao.model.User;
import com.feng.learn.exception.ServiceException;
import com.feng.learn.rpc.ExternalService;
import com.feng.learn.service.ServiceA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceAImpl implements ServiceA {

    @Autowired
    UserDao userDao;

    @Autowired
    ExternalService externalService;

    @Override
    public int service(int userId) throws ServiceException {

        // 依赖外部服务
        String result = externalService.service(userId);

        // 依赖DB（幂等性操作）
        User user = userDao.getById(userId);
        if (user == null) {
            throw new ServiceException();
        }

        // some business code here

        // 静态方法
        String systemAge = System.getProperty("system_age", "0");

        return Math.abs(Integer.valueOf(systemAge) + Integer.valueOf(result) + user.getAge());
    }

    @Override
    public long createUser(String name, int age) {
        // 依赖db，且非幂等行操作
        User u = User.builder().name(name).age(age).build();
        userDao.createUser(u);
        return u.getId();
    }
}
