package com.github.learn.batchcache.domain.service;

import com.github.learn.batchcache.domain.model.User;
import com.github.learn.batchcache.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2020/7/24
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public void create(User user) {
    userRepository.insert(user);
  }
}
