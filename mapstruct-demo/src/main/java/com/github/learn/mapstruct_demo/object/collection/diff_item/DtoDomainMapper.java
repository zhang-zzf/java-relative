package com.github.learn.mapstruct_demo.object.collection.diff_item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author zhanfeng.zhang
 * @date 2021/11/16
 */
@Mapper
public interface DtoDomainMapper {

  DtoDomainMapper mapper = Mappers.getMapper(DtoDomainMapper.class);

  /**
   * <pre>
   * 模型中的集合 List / Set  中的类型
   * Person <-> PersonDto
   * 1. 若定义了转换方法，使用。如 {@link DtoDomainMapper#doDomain(PersonDto)}
   * 2. 若没有定义，会自动转换
   * </pre>
   */
  Dto toDto(Domain domain);

  Domain toDomain(Dto dto);

  @Mapping(target = "name")
  Person doDomain(PersonDto dto);

}
