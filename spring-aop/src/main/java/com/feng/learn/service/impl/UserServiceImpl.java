package com.feng.learn.service.impl;

import static com.feng.learn.util.Trace.objectHex;

import com.feng.learn.dao.UserDao;
import com.feng.learn.model.User;
import com.feng.learn.service.UserService;
import com.feng.learn.util.Trace;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User get(long id) {
        User user = userDao.get(id);
        return user;
    }

    @Override
    public boolean save(long id, String name) {
        Trace.add(String.format("0x%016X", Objects.hashCode(this)));
        log.info("save -> obj: {}", objectHex(this));
        return userDao.save(id, name);
    }

    @Override
    public void throwException(boolean flag) {
        if (flag) {
            throw new IllegalArgumentException("userServiceImpl");
        }
    }

}
