package com.github.learn.mapstruct_demo.object.json;

import com.alibaba.fastjson.JSON;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author zhanfeng.zhang
 * @date 2021/11/16
 */
@Mapper(uses = {ToJSONString.class})
public interface DtoDomainMapper {

  DtoDomainMapper INSTANCE = Mappers.getMapper(DtoDomainMapper.class);

  @Mapping(target = "person", qualifiedByName = "toJSONStr")
  @Mapping(target = "mapData", qualifiedByName = "toJSONStr")
  Dto toDto(Domain domain);

  Domain toDomain(Dto dto);

  default Person stringToPerson(String json) {
    return JSON.parseObject(json, Person.class);
  }

  default Map<String, Object> stringToMap(String json) {
    return JSON.parseObject(json);
  }


}
