package com.feng.learn.springvalidation.domain.repo;

import com.feng.learn.springvalidation.domain.model.Person;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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


}
