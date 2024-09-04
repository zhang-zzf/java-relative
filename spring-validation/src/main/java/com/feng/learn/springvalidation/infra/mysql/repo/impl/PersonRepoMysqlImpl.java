package com.feng.learn.springvalidation.infra.mysql.repo.impl;

import com.feng.learn.springvalidation.domain.model.Person;
import com.feng.learn.springvalidation.domain.repo.PersonRepo;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

/**
 * @author zhanfeng.zhang
 * @date 2021/07/10
 */
@Repository
@Validated
public class PersonRepoMysqlImpl implements PersonRepo {

  @Override
  public Person getById(Long id) {
    if (id == null || id < 10) {
      return null;
    }
    return new Person().setId(id);
  }

  @Override
  public int updatePerson(Person person) {
    return 0;
  }

  @Override
  public List<Person> batchGetById(@NotNull @Size(min = 1, max = 2) List<Long> idList) {
    return Collections.emptyList();
  }

}
