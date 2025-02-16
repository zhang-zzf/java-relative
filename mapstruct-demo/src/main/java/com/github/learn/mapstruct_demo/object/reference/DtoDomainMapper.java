package com.github.learn.mapstruct_demo.object.reference;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author zhanfeng.zhang
 * @date 2021/11/16
 */
@Mapper
public interface DtoDomainMapper {

    DtoDomainMapper mapper = Mappers.getMapper(DtoDomainMapper.class);
    // cohesive

    // Dto 与 Domain 在转换时 copy 的 person 的 reference (浅copy)

    Dto toDto(Domain domain);

    Domain toDomain(Dto dto);

}
