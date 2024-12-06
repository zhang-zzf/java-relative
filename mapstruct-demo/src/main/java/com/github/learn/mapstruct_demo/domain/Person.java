package com.github.learn.mapstruct_demo.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 骑手人
 *
 * @author zhanfeng.zhang
 * @date 2021/06/04
 */
@Data
@Accessors(chain = true)
public class Person {

    public static final String AUTH_STATUS_NO = "未认证";
    public static final String AUTH_STATUS_OK = "已认证";
    public static final String AUTH_STATUS_EXPIRE = "已过期";

    public static final String CROWD_SHIPPING_MODE_YX = "优选";
    public static final String CROWD_SHIPPING_MODE_TC = "同城";
    public static final String CROWD_SHIPPING_MODE_PTZB = "普通众包";
    public static final String RUNODD_CITY_ID = "runodd_city_id";
    public static final String RUNODD_CITY_NAME = "runodd_city_name";
    public static final String APPRAISE = "appraise_json";
    public static final List<String> PERSON_STATISTIC_ARRAY_FIELD = Arrays.asList(
        RUNODD_CITY_ID,
        RUNODD_CITY_NAME
    );
    private Long personId;
    private Set<Account> account;
    /**
     * 账户状态
     * <p>正常</p>
     * <p>注销:人下所有的账号都是注销</p>
     */
    private String accountStatus;
    private Long activeAccountKnightId;
    private Register register;
    private Identity identity;
    /**
     * 基本信息
     */
    private PersonBasic personBasic;
    /**
     * <p>统计数据</p>
     *
     * <p>骑手跑单城市id：runodd_city_id 以 JSON 数组表示 [1, 2, 3]</p>
     * <p>骑手跑单城市名称：runodd_city_name 以 JSON 数组表示 ["上海", "北京"]</p>
     * <p>活跃状态: active_status</p>
     */
    private Map<String, String> statistic;

    /**
     * 健康证,可能为空表示未上传健康证
     * <p>may be null</p>
     */
    private Health health;

    /**
     * 残疾证
     * <p>may be null</p>
     */
    private Disability disability;

    /**
     * 紧急联系人
     */
    private List<EmergencyContact> emergencyContact;

    /**
     * 工作经历，不用
     */
    private List<Experience> experience;

    /**
     * 保证金
     */
    private List<Deposit> depositList;

    /**
     * 驾驶证
     */
    private List<DriverLicense> driverLicenseList;

    /**
     * 行驶证
     */
    private List<VehicleLicense> vehicleLicenseList;

    /**
     * 装备
     */
    private List<Equipment> equipment;

    /**
     * 查询当前运力线
     */
    public Account queryActiveAccount() {
        if (activeAccountKnightId == null) {
            return null;
        }
        return account.stream()
            .filter(a -> a.getKnightId().equals(activeAccountKnightId))
            .findAny()
            .orElse(null);
    }

    public Person setIdentity(Identity identity) {
        if (identity == null) {
            throw new IllegalArgumentException();
        }
        this.identity = identity;
        return this;
    }

    public Person setRegister(Register register) {
        if (register == null) {
            throw new IllegalArgumentException();
        }
        this.register = register;
        return this;
    }

    /**
     * 查询是否是众包账户
     *
     * @param knightId 账户id
     * @return true; false
     */
    public boolean queryIsCrowdAccount(Long knightId) {
        return queryAccountByKnightId(knightId)
            .map(Account::queryIsCrowd)
            .orElse(Boolean.FALSE);
    }

    public boolean queryIsFn2Account(Long knightId) {
        return queryAccountByKnightId(knightId)
            .map(Account::queryIsFn2)
            .orElse(Boolean.FALSE);
    }

    public Optional<Account> queryAccountByKnightId(Long knightId) {
        if (getAccount() == null) {
            return Optional.empty();
        }
        return getAccount().stream()
            .filter(a -> a.getKnightId().equals(knightId))
            .findAny();
    }

    /**
     * 骑手过去的工作经验（简历）
     */
    @Data
    @Accessors(chain = true)
    public static class Experience {

        List<ExperiencePeriod> experiencePeriod;
        /**
         * 来源
         */
        private String source;
        /**
         * 工作年限
         */
        private Integer workYear;

        /**
         * 骑手过去的工作经验（简历）
         */
        @Data
        @Accessors(chain = true)
        public static class ExperiencePeriod {

            private LocalDateTime start;
            private LocalDateTime end;
            private String companyName;
            /**
             * 行业
             */
            private String industry;
            private String workYear;
            /**
             * 职责
             */
            private String job;
            private String workAddress;
        }
    }

    /**
     * 残疾证信息
     */
    @Data
    @Accessors(chain = true)
    public static class Disability {

        /**
         * 残疾类型
         */
        private String type;
        /**
         * 残疾等级
         */
        private String level;
        /**
         * 认证状态
         */
        private String authStatus;
        /**
         * 同意告知顾客
         */
        private Boolean isToldCustomer;
        /**
         * 生效日期
         */
        private LocalDateTime start;
        /**
         * 失效日期
         */
        private LocalDateTime end;
        /**
         * 原件照片
         */
        private String rawPhoto;
    }

    /**
     * 健康证信息
     */
    @Data
    @Accessors(chain = true)
    public static class Health {

        /**
         * 健康证号
         */
        private String number;
        /**
         * 认证状态
         */
        private String authStatus;
        /**
         * 生效日期
         */
        private LocalDateTime start;
        /**
         * 失效日期
         */
        private LocalDateTime end;

        private Long cityId;
        /**
         * 发证城市
         */
        private String city;
        /**
         * 正面照片
         */
        private String frontPhotoHash;
        /**
         * 背面
         */
        private String backPhotoHash;
        /**
         * 手持照片
         */
        private String handTakePhotoHash;
    }

    /**
     * 紧急联系人信息
     */
    @Data
    @Accessors(chain = true)
    public static class EmergencyContact {

        /**
         * 关系
         */
        private String relation;
        /**
         * 姓名
         */
        private String name;
        /**
         * 手机号码
         */
        private String mobile;
    }

    /**
     * 保证金信息
     */
    @Data
    @Accessors(chain = true)
    public static class Deposit {

        /**
         * knightId
         */
        private Long knightId;
        /**
         * 操作时间
         */
        private LocalDateTime operateTime;
        /**
         * 金额
         */
        private String amount;
        /**
         * 操作类型
         */
        private String operateType;
    }

    /**
     * 以 knightId 唯一决定一个 Account
     */
    @Data
    @Accessors(chain = true)
    public static class Account {

        public static final String SUB_TYPE_CROWD = "众包";
        public static final String SUB_TYPE_FN2 = "团队";
        public static final String STATUS_NORMAL = "正常";
        public static final String STATUS_CLOSED = "注销";

        public static final String SHIPPING_MODE_PT = "普通众包";
        public static final String SHIPPING_MODE_YX = "优选骑手";
        public static final String SHIPPING_MODE_TC = "同城众包";
        public static final String SHIPPING_MODE_TD_ZS = "专送骑手";

        private Long knightId;
        /**
         * 运力线
         * <p>众包</p>
         * <p>团队</p>
         */
        private String subType;
        /**
         * 子运力线
         * <p>团队只有 专送骑手 </p>
         * <p>众包有： 普通众包、优选骑手、同城骑士</p>
         */
        private String shippingMode;
        private Long subId;
        private String mobile;
        /**
         * 账户状态
         * <p>正常</p>
         * <p>注销</p>
         */
        private String status;
        /**
         * 账户注册时间
         */
        private LocalDateTime registerAt;
        /**
         * 账户注册城市
         */
        private Long registerCityId;
        private String registerCityName;
        /**
         * 账号开工城市
         */
        private Long workCityId;
        private String workCityName;
        /**
         * 账户注销时间
         */
        private LocalDateTime closedAt;
        /**
         * 接单资格 todo
         */
        private Boolean ability;
        /**
         * 资金账户状态 todo
         */
        private String cashAccountStatus;
        /**
         * 拉灰状态
         */
        private String grayStatus;
        /**
         * 解除拉灰日期
         */
        private LocalDateTime grayEndTime;
        /**
         * 登录设备 todo
         */
        private String loginEquipment;

        /**
         * 头像照片hash
         */
        private String avatarHash;

        public static String intSubTypeToStringSubType(int subType) {
            return subType == 1 ? SUB_TYPE_FN2 : subType == 5 ? SUB_TYPE_CROWD : "未知";
        }

        public static int stringSubTypeToIntSubType(String subType) {
            return subType == SUB_TYPE_FN2 ? 1 : subType == SUB_TYPE_CROWD ? 5 : -1;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Account account = (Account) o;
            return Objects.equals(getKnightId(), account.getKnightId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getKnightId());
        }

        /**
         * 是否是众包账号
         */
        public boolean queryIsCrowd() {
            return SUB_TYPE_CROWD.equals(subType);
        }

        /**
         * 团队账号
         */
        public boolean queryIsFn2() {
            return SUB_TYPE_FN2.equals(subType);
        }

        /**
         * 账号状态正常
         *
         * @return true
         */
        public boolean queryIsActiveAccount() {
            return STATUS_NORMAL.equals(getStatus());
        }
    }

    /**
     * 骑手首次注册信息
     */
    @Data
    @Accessors(chain = true)
    public static class Register {

        private Long knightId;
        private Long cityId;
        private String cityName;
        private String subType;
        private LocalDateTime createdAt;
    }

    @Data
    @Accessors(chain = true)
    public static class Identity {

        public static final String SEX_MALE = "男";
        public static final String SEX_FEMALE = "女";

        private String number;
        private String name;
        private String sex;
        private String ethnic;
        private String province;
        private Long cityId;
        private String city;
        private String area;
        /**
         * 出生日期
         */
        private LocalDate birthDay;
        /**
         * 实名认证状态
         */
        private String authStatus;
        /**
         * 身份证正面照片
         */
        private String frontPhotoHash;
        /**
         * 身份证背面照片
         */
        private String backPhotoHash;
        /**
         * 身份证有效期
         */
        private LocalDateTime from;
        /**
         * 身份证有效期
         */
        private LocalDateTime to;
    }

    /**
     * 基础信息
     */
    @Data
    @Accessors(chain = true)
    public static class PersonBasic {

        /**
         * 身高
         */
        private String height;
        /**
         * 体重
         */
        private String weight;
        /**
         * 尺码
         */
        private String clozeSize;
        /**
         * 学历
         */
        private String topDegree;
        /**
         * 是否已婚
         */
        private Boolean isMarried;
        /**
         * 是否已育
         */
        private Boolean hasChild;
        /**
         * 是否退役军人
         */
        private Boolean isVeteran;
        /**
         * 政治面貌
         */
        private String politicalStatus;
    }

    /**
     * 驾驶证
     */
    @Data
    @Accessors(chain = true)
    public static class DriverLicense {

        /**
         * 证件号
         */
        private String cardNo;
        /**
         * 开始
         */
        private LocalDateTime start;
        /**
         * 结束
         */
        private LocalDateTime end;
        /**
         * 领证时间
         */
        private LocalDateTime takeTime;
        /**
         * 车型
         */
        private String model;
        /**
         * 认证状态
         */
        private String authStatus;
        /**
         * 正面照片
         */
        private String frontPhoto;
        /**
         * 反面照片
         */
        private String backPhoto;
    }

    /**
     * 行驶证
     */
    @Data
    @Accessors(chain = true)
    public static class VehicleLicense {

        /**
         * 车牌号
         */
        private String vehicleNo;
        /**
         * 车辆类型
         */
        private String vehicleType;
        /**
         * 使用性质
         */
        private String useType;
        /**
         * 开始
         */
        private LocalDateTime start;
        /**
         * 结束
         */
        private LocalDateTime end;
        /**
         * 领证时间
         */
        private LocalDateTime takeTime;
        /**
         * 认证状态
         */
        private String authStatus;
        /**
         * 车辆识别码
         */
        private String vin;
        /**
         * 行驶证号
         */
        private String vehicleLicenseNo;
        /**
         * 车头照片
         */
        private String frontPhoto;
        /**
         * 车尾照片
         */
        private String backPhoto;
        /**
         * 车侧后方45度
         */
        private String sidePhoto;
    }

    /**
     * 装备信息
     */
    @Data
    @Accessors(chain = true)
    public static class Equipment {

        /**
         * 装备编号
         */
        private String equipNo;
        /**
         * 装备类型
         * <p>例如：夏装、冬装、头盔、保温箱</p>
         */
        private String equipType;
        /**
         * 激活时间
         */
        private LocalDateTime activeTime;
        /**
         * 获取方式
         * <p>例如：商城购买、自有装备、领取</p>
         */
        private String from;
    }

    /**
     * 合同信息
     */
    @Data
    @Accessors(chain = true)
    public static class Contract {

        /**
         * 合同名称
         */
        private String name;
        /**
         * 合同编号
         */
        private String number;
        /**
         * 签署方式
         */
        private String signType;
        /**
         * 签署主体
         */
        private String signCompany;
        /**
         * 签署时间
         */
        private LocalDateTime signTime;
        /**
         * 合同结束时间
         */
        private LocalDateTime signEndTime;
        /**
         * 原件
         */
        private String rawPhoto;
    }

}
