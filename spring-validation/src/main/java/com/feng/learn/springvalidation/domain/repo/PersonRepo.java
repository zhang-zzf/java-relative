package com.feng.learn.springvalidation.domain.repo;

import com.feng.learn.springvalidation.domain.model.Person;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

/**
 * @author zhanfeng.zhang
 * @date 2021/07/10
 */
@Validated
public interface PersonRepo {

  /**
   * get by id
   *
   * @param id id
   * @return data
   */
  @NotNull Person getById(@NotNull Long id);

  /**
   * update person
   *
   * @param person data
   * @return updated number
   */
  int updatePerson(@NotNull @Valid Person person);

  /**
   * batch get by id
   *
   * @param idList id
   * @return data
   */
  List<Person> batchGetById(@NotNull @Size(min = 1, max = 2) List<Long> idList);


}
