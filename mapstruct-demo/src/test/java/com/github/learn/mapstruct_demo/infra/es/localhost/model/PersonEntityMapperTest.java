package com.github.learn.mapstruct_demo.infra.es.localhost.model;

import static org.assertj.core.api.BDDAssertions.then;

import com.github.learn.mapstruct_demo.domain.Person;
import com.github.learn.mapstruct_demo.domain.Person.Identity;
import com.github.learn.mapstruct_demo.domain.Person.Register;
import org.junit.jupiter.api.Test;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/11
 */
class PersonEntityMapperTest {

    @Test
    public void given_whenToEntityAndToDomain_thenSuccess() {
        final Person person = new Person()
            .setPersonId(1L)
            .setIdentity(new Identity())
            .setRegister(new Register());
        final PersonEntity entity = PersonEntityMapper.INSTANCE.fromDomain(person);
        then(entity).returns(1L, PersonEntity::getPersonId);
        final Person toDomain = PersonEntityMapper.INSTANCE.toDomain(entity);
        then(toDomain).returns(1L, Person::getPersonId);
    }

}