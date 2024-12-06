package com.feng.learn.springvalidation.infra.mysql.repo.impl;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import com.feng.learn.springvalidation.domain.model.Person;
import com.feng.learn.springvalidation.domain.model.Person.Address;
import com.feng.learn.springvalidation.domain.repo.PersonRepo;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhanfeng.zhang
 * @date 2021/07/10
 */
@SpringBootTest
class PersonRepoMysqlImpl_IT {

    @Autowired
    PersonRepo personRepo;

    /**
     * <p>接口上标注的 @NotNull 等注解是生效的</p>
     * <p>接口上标注 @Validated，方法上标注 @NotNull</p>
     * <p>无需在实现类上添加注解</p>
     */
    @Test
    void givenNull_whenGetById_thenFail() {
        final Throwable throwable = catchThrowable(() -> personRepo.getById(null));
        then(throwable).isNotNull();
    }

    @Test
    void givenNullAddressProvince_whenValidateMethodObjectField_thenFail() {
        final Person person = new Person().setId(2L).setAddress(new Address());
        final Throwable throwable = catchThrowable(() -> personRepo.updatePerson(person));
        then(throwable).isNotNull();
    }

    @Test
    void given_whenValidateMethodReturnObject_thenFail() {
        final Throwable throwable = catchThrowable(() -> personRepo.getById(5L));
        then(throwable).isNotNull();
    }

    @Test
    void givenNullList_whenBatchGetById_thenFail() {
        final Throwable throwable = catchThrowable(() -> personRepo.batchGetById(null));
        then(throwable).isNotNull();
    }

    @Test
    void givenEmptyList_whenBatchGetById_thenFail() {
        final Throwable throwable = catchThrowable(() -> personRepo.batchGetById(new ArrayList<>()));
        then(throwable).isNotNull();
    }


}