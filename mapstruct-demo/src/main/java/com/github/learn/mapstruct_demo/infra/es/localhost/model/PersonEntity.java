package com.github.learn.mapstruct_demo.infra.es.localhost.model;

import com.github.learn.mapstruct_demo.domain.Person.Account;
import com.github.learn.mapstruct_demo.domain.Person.Disability;
import com.github.learn.mapstruct_demo.domain.Person.Equipment;
import com.github.learn.mapstruct_demo.domain.Person.Health;
import com.github.learn.mapstruct_demo.domain.Person.Identity;
import com.github.learn.mapstruct_demo.domain.Person.Register;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>依赖领域层模型</p>
 *
 * @author zhanfeng.zhang
 * @date 2021/06/11
 */
@Data
@Accessors(chain = true)
public class PersonEntity {

    private Long personId;

    private Set<Account> account;

    private Account activeAccount;

    /**
     * 账户状态
     * <p>正常</p>
     * <p>注销:人下所有的账号都是注销</p>
     */
    private String accountStatus;

    private Register register;

    private Identity identity;

    private Map<String, Object> statistic;

    private Health health;

    private Disability disability;

    /**
     * 装备
     */
    private List<Equipment> equipment;

}
