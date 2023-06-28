package com.github.learn.mapstruct_demo.object.method.reference;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * @author zhanfeng.zhang
 * @date 2021/11/16
 */
@Mapper
public interface DtoDomainMapper {

  DtoDomainMapper mapper = Mappers.getMapper(DtoDomainMapper.class);

  // Dto 与 Domain 在转换时 copy 的 person 的 reference (浅copy)

  @Mapping(target = "id", source = "domain", qualifiedByName = {"domain#queryName"})
  Dto toDto(Domain domain);

  Domain toDomain(Dto dto);

  @Named("domain#queryId")
  default String queryDomainId(Domain domain) {
    return domain.queryId();
  }

  @Named("domain#queryName")
  default String queryDomainName(Domain domain) {
    return domain.queryId();
  }

}
