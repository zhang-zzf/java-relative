package com.github.learn.domain.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

import com.github.learn.AbstractJUnit4Mockito;
import com.github.learn.domain.model.User;
import com.github.learn.domain.repo.UserRepo;
import java.util.Optional;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * @author zhanfeng.zhang
 * @date 2021/07/04
 */
public class UserServiceTest extends AbstractJUnit4Mockito {

  @InjectMocks
  UserService userService;

  @Mock
  UserRepo userRepo;

  @Test
  public void getNameById() {
    // given
    final String name = "zhang.zzf";
    // stub
    Long id = 1L;
    given(userRepo.getById(any())).willReturn(Optional.of(buildUser(name, id)));
    // when
    final Optional<User> byId = userService.getById(1L);
    // then
    // mockito verify
    BDDMockito.then(userRepo).should(times(1)).getById(1L);
    // result assert
    BDDAssertions.then(byId).isPresent().get().returns(id, User::getId)
        .returns(name, User::getName);
  }

  private User buildUser(String name, Long id) {
    return User.builder().id(id).name(name).build();
  }
}