package com.github.learn.mapstruct_demo.infra.es.localhost.model;

import com.github.learn.mapstruct_demo.domain.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/11
 */
@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    /**
     * toEntity
     *
     * @param domain data
     * @return entity
     */
    @Mapping(ignore = true, target = "statistic")
    PersonEntity toEntity(Person domain);

    /**
     * toDomain
     *
     * @param entity data
     * @return domain model
     */
    @Mapping(ignore = true, target = "statistic")
    Person toDomain(PersonEntity entity);

}
