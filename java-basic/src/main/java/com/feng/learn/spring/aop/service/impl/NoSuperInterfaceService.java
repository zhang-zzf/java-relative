package com.feng.learn.spring.aop.service.impl;

import com.feng.learn.dao.model.User;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2020/6/7
 */
@Service
public class NoSuperInterfaceService {

  public User service() {
    return null;
  }
}
