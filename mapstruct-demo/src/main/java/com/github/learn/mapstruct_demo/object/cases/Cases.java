package com.github.learn.mapstruct_demo.object.cases;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

public class Cases {


  /**
   * <pre>
   *   1. id -> _id
   *   2. nothing -> id
   * </pre>
   */
  @Mapper
  public interface DtoDomainMapper {

    DtoDomainMapper INSTANCE = Mappers.getMapper(DtoDomainMapper.class);

    /**
     * <pre>
     * {@link Dto#id} -> {@link Domain#_id}
     * nothing -> {@link Domain#id} 取消映射
     * </pre>
     */
    @Mapping(target = "_id", source = "id")
    @Mapping(target = "id", ignore = true)
    Domain toDomain(Dto dto);


    @Mapping(target = "id", source = "_id")
    Dto toDto(Domain domain);


    @Data
    @Accessors(chain = true)
    class Domain {

      private Long id;
      private String _id;
      private String str;

      public void set_id(String id) {
        this._id = id;
      }

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Dto {

      private String str;

      private String id;

    }

  }

}
