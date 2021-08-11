package com.github.learn.mapstruct_demo.infra.es.localhost.model;

import com.github.learn.mapstruct_demo.domain.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/11
 */
@Mapper
public interface PersonEntityMapper {

    PersonEntityMapper INSTANCE = Mappers.getMapper(PersonEntityMapper.class);

    /**
     * toEntity
     *
     * @param domain data
     * @return entity
     */
    PersonEntity fromDomain(Person domain);

    /**
     * toDomain
     *
     * @param entity data
     * @return domain model
     */
    Person toDomain(PersonEntity entity);

}
