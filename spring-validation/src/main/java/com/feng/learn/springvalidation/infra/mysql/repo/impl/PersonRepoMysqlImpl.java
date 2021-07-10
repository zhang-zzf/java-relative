package com.feng.learn.springvalidation.infra.mysql.repo.impl;

import com.feng.learn.springvalidation.domain.model.Person;
import com.feng.learn.springvalidation.domain.repo.PersonRepo;
import org.springframework.stereotype.Repository;

/**
 * @author zhanfeng.zhang
 * @date 2021/07/10
 */
@Repository
public class PersonRepoMysqlImpl implements PersonRepo {

    @Override
    public Person getById(Long id) {
        if (id < 10) {
            return null;
        }
        return new Person().setId(id);
    }

    @Override
    public int updatePerson(Person person) {
        return 0;
    }

}
