package com.github.learn.domain.service;

import com.github.learn.domain.model.User;
import com.github.learn.domain.repo.UserRepo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author zhanfeng.zhang
 * @date 2021/07/04
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepo userRepo;

  /**
   * getUserNameByUserId
   *
   * @param userId id
   * @return User
   */
  public Optional<User> getById(Long userId) {
    Optional<User> u = userRepo.getById(userId);
    // some algorithm
    return u;
  }


}
