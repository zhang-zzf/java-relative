package com.feng.insure.protocol.insureserver.controller;

import org.springframework.stereotype.Service;

/**
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/12/7
 */
@Service
public class AuthenticationService {

  public boolean authenticate(String username) {
    return true;
  }

}
