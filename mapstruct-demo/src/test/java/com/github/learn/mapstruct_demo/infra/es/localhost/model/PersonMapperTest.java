package com.github.learn.mapstruct_demo.infra.es.localhost.model;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

import com.github.learn.mapstruct_demo.domain.Person;
import org.junit.jupiter.api.Test;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/11
 */
class PersonMapperTest {

    @Test
    public void givenDomain_whenToEntity_thenSuccess() {
        final Person person = new Person()
            .setPersonId(1L);
        final PersonEntity entity = PersonMapper.INSTANCE.toEntity(person);
        then(entity).isNotNull();
    }

}