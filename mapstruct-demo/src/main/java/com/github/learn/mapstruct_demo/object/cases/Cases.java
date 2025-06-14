package com.github.learn.mapstruct_demo.object.cases;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static java.util.Optional.ofNullable;

public class Cases {

    /**
     * <pre>
     *     Map to Bean
     *     Bean to Map
     * </pre>
     */

    /**
     * <pre>
     *      @Builder 模式
     *      DTO -> domain -> PO
     *      DTO <- domain <- PO
     *
     * </pre>
     *
     */
    @Mapper
    public interface BuilderMapper {

        BuilderMapper INSTANCE = Mappers.getMapper(BuilderMapper.class);


        @Getter
        @Builder
        @Jacksonized
        class Bom {
            String bomId;
            /**
             * 成品
             */
            List<Sku> skus;
            /**
             * 原料
             */
            List<Sku> materials;
            /**
             * 扩展信息
             */
            BomExt ext;

            Long version;

            @Getter
            @Builder
            @Jacksonized
            public static class Sku {
                private Long skuId;
                private String skuName;
                // "货品单位"
                private String skuUnit;
                //  "采购分摊比例"
                private BigDecimal apportion;
                // "配比数量"
                private BigDecimal proportion;
            }

            @Getter
            @Builder
            @Jacksonized
            public static class BomExt {

                /**
                 * 是否开启手动制造 true:开启,false:关闭
                 */
                private Boolean manualFlag;

                /**
                 * 是否允许反制造,true:开启,false:关闭
                 */
                private Boolean revoltManufactureFlag;

                /**
                 * 是否开启共享库存
                 */
                private Boolean sharedInventoryFlag;

                /**
                 * 成品扩展信息
                 */
                private List<BomExtGoods> productList;

                /**
                 * 耗材扩展信息
                 */
                private List<BomExtGoods> consumableList;

                @Builder
                @Getter
                @Jacksonized
                public static class BomExtGoods {
                    Long goodsId;
                    String goodsName;
                    BigDecimal goodsQty;
                    Boolean sharedInventoryFlag;
                }

            }
        }

        @Builder
        @Getter
        class ManufactureRule {

            Long id;

            /**
             * 成品
             */
            Sku sku;

            /**
             * 区域
             */
            Area area;

            /**
             * 规则
             */
            Rule rule;

            @Builder
            @Getter
            @Jacksonized
            public static class Area {
                /**
                 * 城市_区域_门店ID 编码
                 */
                String code;
                /**
                 * 名字
                 */
                String name;
            }

            @Getter
            @Builder
            @Jacksonized
            public static class Sku {
                Long skuId;
            }

            @Builder
            @Getter
            @Jacksonized
            public static class Rule {
                // 规则名称
                private String name;
                // 关联的 bom 制造关系
                private Bom bom;
                // 入库完成制造
                private InboundCompleteManufacture inboundCompleteManufacture;
                // 安全库存制造
                private SafetyStockManufacture safetyStockManufacture;
                /**
                 * 创建操作
                 */
                Operate create;
                /**
                 * 更新操作
                 */
                Operate update;

                @Getter
                @Builder
                @Jacksonized
                public static class SafetyStockManufacture {
                    // 成品库存 < thresholdQty，触发制造
                    private BigDecimal thresholdQty;
                    // 制造成品的数量
                    private BigDecimal adviceQty;
                    // 部分制造，如果库存不足，则只制造部分
                    // 原料不足时按剩余原料制造
                    private Boolean enablePartialManufacture;
                }

                @Getter
                @Builder
                @Jacksonized
                public static class InboundCompleteManufacture {
                    //推荐数量过滤阈值,预测量小于该值，忽略制造
                    private BigDecimal thresholdQty;
                    //延时分钟。0-不开启延时制造；n-延时n分钟制造
                    private Integer delayMinutes;
                }

                @Getter
                @Builder
                @Jacksonized
                public static class Operate {
                    // 操作人ID
                    String by;
                    // 操作人姓名
                    String name;
                    // 操作时间
                    Date time;
                }
            }

        }

        @Data
        class BomDTO {
            @NotNull(message = "bomId不能为空")
            String bomId;
            List<Sku> skus;
            /**
             * 原料
             */
            List<Sku> materials;
            /**
             * 扩展信息
             */
            BomExt ext;

            Long version;

            @Data
            public static class Sku {
                private Long skuId;
                private String skuName;
                // "货品单位"
                private String skuUnit;
                //  "采购分摊比例"
                private BigDecimal apportion;
                // "配比数量"
                private BigDecimal proportion;
            }

            @Data
            public static class BomExt {

                /**
                 * 是否开启手动制造 true:开启,false:关闭
                 */
                private Boolean manualFlag;

                /**
                 * 是否允许反制造,true:开启,false:关闭
                 */
                private Boolean revoltManufactureFlag;

                /**
                 * 是否开启共享库存
                 */
                private Boolean sharedInventoryFlag;

                /**
                 * 成品扩展信息
                 */
                private List<BomExtGoods> productList;

                /**
                 * 耗材扩展信息
                 */
                private List<BomExtGoods> consumableList;

                @Data
                public static class BomExtGoods {
                    Long goodsId;
                    String goodsName;
                    BigDecimal goodsQty;
                    Boolean sharedInventoryFlag;
                }

            }
        }

        @Data
        class ManufactureRuleDTO {

            Long id;

            /**
             * 成品
             */
            @NotNull(message = "skuId不能为空")
            Long skuId;
            /**
             * 区域
             */
            @NotNull(message = "areaCode不能为空")
            String areaCode;
            /**
             * 区域
             */
            String areaName;

            // 规则名称
            @NotNull(message = "name不能为空")
            private String name;
            // 关联的 bom 制造关系
            @NotNull(message = "bom不能为空")
            @Valid
            private BomDTO bom;
            // 入库完成制造
            @Valid
            private InboundCompleteManufacture inboundCompleteManufacture;
            // 安全库存制造
            @Valid
            private SafetyStockManufacture safetyStockManufacture;

            // 操作人ID
            String createBy;
            // 操作人姓名
            String createName;
            // 操作时间
            Date createTime;

            /**
             * 更新操作
             */
            String updateBy;
            String updateName;
            Date updateTime;

            @Data
            public static class SafetyStockManufacture {
                // 成品库存 < thresholdQty，触发制造
                @NotNull
                private BigDecimal thresholdQty;
                // 制造成品的数量
                @NotNull
                private BigDecimal adviceQty;
                // 部分制造，如果库存不足，则只制造部分
                // 原料不足时按剩余原料制造
                private Boolean enablePartialManufacture;
            }

            @Data
            public static class InboundCompleteManufacture {
                //推荐数量过滤阈值,预测量小于该值，忽略制造
                @NotNull
                private BigDecimal thresholdQty;
                //延时分钟。0-不开启延时制造；n-延时n分钟制造
                @NotNull
                private Integer delayMinutes;
            }

        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        class ManufactureRulePO {
            /**
             * 唯一标识
             */
            private Long id;

            /**
             * 品id
             */
            private Long skuId;

            /**
             * 区域code
             */
            private String areaCode;

            /**
             * 区域name
             */
            private String areaName;

            /**
             * 配置
             */
            private String rule;
            private Rule ruleObject;

            /**
             * 创建人ID
             */
            private String createBy;

            /**
             * 创建人
             */
            private String createName;

            /**
             * 修改人ID
             */
            private String updateBy;

            /**
             * 修改人
             */
            private String updateName;

            /**
             * 更新时间
             */
            private Date createTime;

            /**
             * 更新时间
             */
            private Date updateTime;

            /**
             * 是否删除(0-否；id-是)
             */
            private Long isDeleted;

            /**
             * 版本号
             */
            private Integer version;

            @Data
            public static class Rule {
                // 规则名称
                private String name;
                // 关联的 bom 制造关系
                private Bom bom;
                // 入库完成制造
                private InboundCompleteManufacture inboundCompleteManufacture;
                // 安全库存制造
                private SafetyStockManufacture safetyStockManufacture;
                /**
                 * 创建操作
                 */
                Operate create;
                /**
                 * 更新操作
                 */
                Operate update;

                @Data
                public static class SafetyStockManufacture {
                    // 成品库存 < thresholdQty，触发制造
                    private BigDecimal thresholdQty;
                    // 制造成品的数量
                    private BigDecimal adviceQty;
                    // 部分制造，如果库存不足，则只制造部分
                    // 原料不足时按剩余原料制造
                    private Boolean enablePartialManufacture;
                }

                @Data
                public static class InboundCompleteManufacture {
                    //推荐数量过滤阈值,预测量小于该值，忽略制造
                    private BigDecimal thresholdQty;
                    //延时分钟。0-不开启延时制造；n-延时n分钟制造
                    private Integer delayMinutes;
                }

                @Data
                public static class Operate {
                    // 操作人ID
                    String by;
                    // 操作人姓名
                    String name;
                    // 操作时间
                    Date time;
                }
            }

        }

    }

    /**
     *
     */

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
