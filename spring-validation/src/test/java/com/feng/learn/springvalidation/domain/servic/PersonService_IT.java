package com.feng.learn.springvalidation.domain.servic;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import com.feng.learn.springvalidation.domain.model.Person;
import com.feng.learn.springvalidation.domain.model.Person.Address;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolationException;
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
    void givenList_whenValidate_then() {
        then(catchThrowable(() -> personService.batchGetById(null)))
            .isInstanceOf(ConstraintViolationException.class)
            .returns("batchGetById.idList: must not be null", Throwable::getMessage)
        ;
        then(catchThrowable(() -> personService.batchGetById(new ArrayList<>())))
            .isInstanceOf(ConstraintViolationException.class)
            .returns("batchGetById.idList: size must be between 1 and 2147483647", Throwable::getMessage)
        ;
        /**
         * List 对象中塞入 null 不会抛错
         */
        List<Long> list = new ArrayList<Long>() {{
            add(null);
        }};
        then(list.size()).isEqualTo(1);
        // @NotNull @Size(min = 1) List<Long> idList
        then(catchThrowable(() -> personService.batchGetById(list))).isNull();
        // @NotNull @Size(min = 1) List<@NotNull Long> idList
        then(catchThrowable(() -> personService.batchGetById2(list)))
            .isInstanceOf(ConstraintViolationException.class)
            .returns("batchGetById2.idList[0].<list element>: must not be null", Throwable::getMessage)
        ;
        //
        List<Person> personList = new ArrayList<Person>() {{
            add(new Person());
        }};
        // @NotNull @Size(min = 1) List<@NotNull Person> personList
        // 不会级联校验 Person 对象
        then(catchThrowable(() -> personService.updatePerson2(personList))).isNull();
        // @NotNull @Size(min = 1) List<@NotNull @Valid Person> personList
        // 级联校验 Person 对象
        Throwable t1 = catchThrowable(() -> personService.updatePerson3(personList));
        then(t1).isInstanceOf(ConstraintViolationException.class);
        then(t1.getMessage()).containsSequence("updatePerson3.personList[0].id: must not be null")
        ;
        // @NotNull @Size(min = 1) @Valid List<@NotNull Person> personList
        // 级联校验 Person 对象
        Throwable t2 = catchThrowable(() -> personService.updatePerson4(personList));
        then(t2).isInstanceOf(ConstraintViolationException.class);
        then(t2.getMessage()).contains("updatePerson4.personList[0].id: must not be null")
        ;
    }

    @Test
    void given_whenValidateMethodParam_then() {
        then(personService.getById(10L)).isNotNull();
        then(catchThrowable(() -> personService.getById(null)))
            .isInstanceOf(ConstraintViolationException.class)
            .returns("getById.id: must not be null", Throwable::getMessage)
        ;
        then(catchThrowable(() -> personService.getById(-1L)))
            .isInstanceOf(ConstraintViolationException.class)
            .returns("getById.id: must be between 0 and 9223372036854775807", Throwable::getMessage)
        ;
    }

    @Test
    void given_whenValidateMethodRet_then() {
        then(catchThrowable(() -> personService.getById(9L)))
            .isInstanceOf(ConstraintViolationException.class)
            .returns("getById.<return value>: must not be null", Throwable::getMessage);
    }

    @Test
    void givenObjectParam_whenValidateMethodObjectParam_thenFail() {
        // @NotNull
        then(catchThrowable(() -> personService.updatePerson(null)))
            .isInstanceOf(ConstraintViolationException.class)
            .returns("updatePerson.person: must not be null", Throwable::getMessage);
        // @Valid 级联校验
        Throwable t1 = catchThrowable(() -> personService.updatePerson(new Person()));
        then(t1).isInstanceOf(ConstraintViolationException.class);
        then(t1.getMessage()).contains("updatePerson.person.id: must not be null")
        ;
    }

    @Test
    void given0_whenValidateMethodObjectField_thenFail() {
        final Person person = new Person().setId(-1L)
            .setAddress(new Address());
        final Throwable throwable = catchThrowable(() -> personService.updatePerson(person));
        then(throwable).isNotNull().isInstanceOf(ConstraintViolationException.class);
        then(throwable.getMessage()).contains("updatePerson.person.address.province: must not be blank")
        ;
    }

    @Test
    void givenNullAddressProvince_whenValidateMethodObjectField_thenFail() {
        final Person person = new Person().setId(2L).setAddress(new Address());
        final Throwable throwable = catchThrowable(() -> personService.updatePerson(person));
        then(throwable).isNotNull()
            .isInstanceOf(ConstraintViolationException.class)
            .returns("updatePerson.person.address.province: must not be blank", Throwable::getMessage);
    }


}