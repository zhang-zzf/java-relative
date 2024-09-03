package com.feng.learn.controller;

import com.feng.learn.model.User;
import com.feng.learn.service.UserService;
import com.feng.learn.service.impl.NoSuperInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private NoSuperInterfaceService noSuperInterfaceService;

  @RequestMapping("/user/get")
  public User get(long id) {
    noSuperInterfaceService.service();
    User user = userService.get(id);
    return user;
  }

  @RequestMapping("/user/save")
  public boolean save(long id, String name) {
    return userService.save(id, name);
  }
}
