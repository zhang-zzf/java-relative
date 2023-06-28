package com.feng.learn.spring.aop.service.impl;

import com.feng.learn.dao.model.User;
import com.feng.learn.spring.aop.service.UserService;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2020/6/6
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

  @Override
  public User get(long id) {
    log.info("UserServiceImpl: this=>{}", Objects.hashCode(this));
    return new User();
  }

  @Override
  public boolean save(long id, String name) {
    log.info("UserServiceImpl: this=>{}", Objects.hashCode(this));
    return true;
  }

  @Override
  public void throwException(boolean b) {
    log.info("UserServiceImpl: this=>{}", Objects.hashCode(this));
    if (b) {
      throw new IllegalArgumentException("userServiceImpl");
    }
  }

}
