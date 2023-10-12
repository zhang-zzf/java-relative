package com.github.learn.mapstruct_demo.object.cases;

import static org.assertj.core.api.BDDAssertions.then;

import com.github.learn.mapstruct_demo.object.cases.Cases.DtoDomainMapper;
import com.github.learn.mapstruct_demo.object.cases.Cases.DtoDomainMapper.Domain;
import com.github.learn.mapstruct_demo.object.cases.Cases.DtoDomainMapper.Dto;
import org.junit.jupiter.api.Test;

class CasesTest {


  /**
   * <pre>
   * a 字段 -> b 字段
   * 无 字段 -> a 字段
   * </pre>
   */
  @Test
  void given_别名_忽略_when_then() {
    DtoDomainMapper mapper = DtoDomainMapper.INSTANCE;
    String aId = "aId";
    Domain domain = mapper.toDomain(new Dto("str", aId));
    then(domain).returns(aId, Domain::get_id).returns(null, Domain::getId);
    Dto dto = mapper.toDto(domain);
    then(dto).returns(aId, Dto::getId);
  }

}