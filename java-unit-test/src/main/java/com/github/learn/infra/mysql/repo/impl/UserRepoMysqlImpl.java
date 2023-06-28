package com.github.learn.infra.mysql.repo.impl;

import com.github.learn.domain.model.User;
import com.github.learn.domain.repo.UserRepo;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * @author zhanfeng.zhang
 * @date 2021/07/04
 */
@Repository
public class UserRepoMysqlImpl implements UserRepo {

  @Override
  public Optional<User> getById(Long userId) {
    return Optional.empty();
  }
}
