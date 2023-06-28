package com.feng.learn.springvalidation.domain.servic;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import com.feng.learn.springvalidation.domain.model.Person;
import com.feng.learn.springvalidation.domain.model.Person.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhanfeng.zhang
 * @date 2021/07/10
 */
@SpringBootTest
class PersonService_IT {

  @Autowired
  PersonService personService;

  @Test
  void given_whenValidateMethodParam_then() {
    final Person byId = personService.getById(1L);
    then(byId).isNotNull();
  }

  @Test
  void givenNull_whenValidateMethodParam_thenFail() {
    final Throwable throwable = catchThrowable(() -> personService.getById(null));
    then(throwable).isNotNull();
  }

  @Test
  void givenNull_whenValidateMethodObjectParam_thenFail() {
    final Throwable throwable = catchThrowable(() -> personService.updatePerson(null));
    then(throwable).isNotNull();
  }

  @Test
  void givenNull_whenValidateMethodObjectField_thenFail() {
    final Throwable throwable = catchThrowable(() -> personService.updatePerson(new Person()));
    then(throwable).isNotNull();
  }

  @Test
  void given0_whenValidateMethodObjectField_thenFail() {
    final Person person = new Person().setId(-1L)
        .setAddress(new Address());
    final Throwable throwable = catchThrowable(() -> personService.updatePerson(person));
    then(throwable).isNotNull();
  }

  @Test
  void givenNullAddressProvince_whenValidateMethodObjectField_thenFail() {
    final Person person = new Person().setId(2L).setAddress(new Address());
    final Throwable throwable = catchThrowable(() -> personService.updatePerson(person));
    then(throwable).isNotNull();
  }


}