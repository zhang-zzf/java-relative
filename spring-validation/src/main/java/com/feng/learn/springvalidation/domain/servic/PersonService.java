package com.feng.learn.springvalidation.domain.servic;

import com.feng.learn.springvalidation.domain.model.Person;
import com.feng.learn.springvalidation.domain.repo.PersonRepo;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author zhanfeng.zhang
 * @date 2021/07/10
 */
@Service
@RequiredArgsConstructor
@Validated
public class PersonService {

  final PersonRepo personRepo;

  public @NotNull Person getById(@NotNull Long id) {
    return personRepo.getById(id);
  }

  public @Range(min = 0L) int updatePerson(@NotNull @Valid Person person) {
    return 0;
  }

}
