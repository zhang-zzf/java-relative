package com.github.learn.mapstruct_demo.object.cases;

import java.math.BigDecimal;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

public class Cases {

    /**
     * <pre>
     *     BigDecimal 格式
     * </pre>
     */
    @Mapper
    public interface NumFormatMapper {

        NumFormatMapper INSTANCE = Mappers.getMapper(NumFormatMapper.class);

        /**
         * 保留2为小数，不存在的以 0 填充
         * <p>仅在 String -> Number / Number -> String 时生效</p>
         * <p>Number -> Number 不生效</p>
         */
        @Mapping(target = "price", numberFormat = "#.##")
        Domain toDomain(Dto dto);

        @Data
        @Accessors(chain = true)
        class Dto {
            BigDecimal price;
        }

        @Data
        @Accessors(chain = true)
        class Domain {
            BigDecimal price;
        }

    }

    /**
     * <pre>
     *     更新存在的 Bean
     * </pre>
     */
    @Mapper
    public interface UpdateExistBeanDtoDomainMapper {

        UpdateExistBeanDtoDomainMapper INSTANCE = Mappers.getMapper(UpdateExistBeanDtoDomainMapper.class);

        /**
         * <pre>
         *     注意： Dto 中有的字段会全部覆盖到 domain 中。
         * </pre>
         */
        @Mapping(target = ".", source = "dto.dto1")
        @Mapping(target = ".", source = "dto.dto2")
        // 忽略 id
        Domain updateExistBean(Dto dto, @MappingTarget Domain domain);

        @Data
        @Accessors(chain = true)
        class Dto {
            private String userName;
            private Dto1 dto1;
            private Dto2 dto2;
        }

        @Data
        @Accessors(chain = true)
        class Domain {
            private Long id;
            private String str;
            private String userName;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        class Dto1 {
            private String str;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        class Dto2 {
            private String id;
        }

    }

    /**
     * <pre>
     *   多个 object 合成一个对象
     *   case1：入参使用字段名字 {@link NToOneDtoDomainMapper#toDomainUseField(Dto1, Long, String, String)}
     *   case2：使用多个 bean 映射到一个 bean {@link NToOneDtoDomainMapper#toDomain(Dto1, Dto2)}
     *   case3: 把 source bean 中多个 nest bean 映射到一个 bean 中
     * </pre>
     */
    @Mapper
    public interface NToOneDtoDomainMapper {

        NToOneDtoDomainMapper INSTANCE = Mappers.getMapper(NToOneDtoDomainMapper.class);

        /**
         * <pre>
         *   1. dto1 中包含 str 字段，优先使用。不会再使用 str 参数。
         *   2. id, useName 参数可以直接映射到 {@link Domain#id}, {@link Domain#userName}
         * </pre>
         */
        // @Mapping(target = "str", source = "str")
        Domain toDomainUseField(Dto1 dto1, Long id, String userName, String str);

        /**
         * <pre>
         *   1. 有 @Mapping，使用 @Mapping 中的映射关系
         *   2. 没有 @Mapping，使用 dto1,dto2 中的字段名字映射到 domain
         *
         * </pre>
         */
        //    @Mapping(target = "id", source = "dto2.id")
        //    @Mapping(target = "str", source = "dto1.str")
        Domain toDomain(Dto1 dto1, Dto2 dto2);

        /**
         * <pre>
         *   unwrap
         *   把 Dto 中的 bean 展平
         * </pre>
         */
        @Mapping(target = ".", source = "dto.dto1")
        @Mapping(target = ".", source = "dto.dto2")
        Domain toDomain(Dto dto);

        @Data
        @Accessors(chain = true)
        class Dto {
            private String userName;
            private Dto1 dto1;
            private Dto2 dto2;
        }

        @Data
        @Accessors(chain = true)
        class Domain {

            private Long id;
            private String str;
            private String userName;

        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        class Dto1 {
            private String str;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        class Dto2 {
            private String id;
        }

    }

    /**
     * <pre>
     *   字段映射
     *   字段忽略
     *   1. id -> _id
     *   2. nothing -> id
     * </pre>
     */
    @Mapper
    public interface DtoDomainMapper {

        DtoDomainMapper INSTANCE = Mappers.getMapper(DtoDomainMapper.class);

        /**
         * <pre>
         *    map to bean
         * </pre>
         */
        @Mapping(target = "str", source = "aStr")
        Domain toDomain(Map<String, Object> map);

        default String toString(Object o) {
            if (o == null) {
                return null;
            }
            return o.toString();
        }

        default Long toLong(Object o) {
            if (o instanceof Long) {
                return (Long) o;
            }
            if (o == null) {
                return null;
            }
            return Long.valueOf(o.toString());
        }

        /**
         * <pre>
         * {@link Domain#_id} <- {@link Dto#id}
         * {@link Domain#id} <- nothing 取消映射
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
