package com.github.learn.mapstruct_demo.object.cases;

import static org.assertj.core.api.BDDAssertions.then;

import com.github.learn.mapstruct_demo.object.cases.Cases.DtoDomainMapper;
import com.github.learn.mapstruct_demo.object.cases.Cases.DtoDomainMapper.Domain;
import com.github.learn.mapstruct_demo.object.cases.Cases.DtoDomainMapper.Dto;
import java.util.HashMap;
import java.util.Map;
import lombok.var;
import org.junit.jupiter.api.Test;

class CasesTest {


  /**
   * Map to Bean
   */
  @Test
  void givenMap_whenConvertToBean_then() {
    DtoDomainMapper mapper = DtoDomainMapper.INSTANCE;
    Map<String, Object> map = new HashMap<String, Object>() {{
      put("aStr", 2);
      put("id", 1L);
    }};
    Domain domain = mapper.toDomain(map);
    then(domain).returns("2", Domain::getStr).returns(1L, Domain::getId);
  }

  /**
   * 更新已存在的 Bean
   */
  @Test
  void givenExistBean_whenUpdate_then() {
    Cases.UpdateExistBeanDtoDomainMapper mapper = Cases.UpdateExistBeanDtoDomainMapper.INSTANCE;
    var domain = new Cases.UpdateExistBeanDtoDomainMapper.Domain().setId(1L);
    Cases.UpdateExistBeanDtoDomainMapper.Dto dto = new Cases.UpdateExistBeanDtoDomainMapper.Dto()
        .setUserName("zhang.zzf")
        .setDto1(new Cases.UpdateExistBeanDtoDomainMapper.Dto1("str"));
    mapper.updateExistBean(dto, domain);
    then(domain).returns("zhang.zzf",
        Cases.UpdateExistBeanDtoDomainMapper.Domain::getUserName);
  }

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