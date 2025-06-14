package com.feng.learn.springvalidation.infra.mysql.repo.impl;

import com.feng.learn.springvalidation.domain.model.Person;
import com.feng.learn.springvalidation.domain.model.Person.Address;
import com.feng.learn.springvalidation.domain.repo.PersonRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.util.*;

import static java.beans.Beans.isInstanceOf;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author zhanfeng.zhang
 * @date 2021/07/10
 */
@SpringBootTest
class PersonRepoMysqlImpl_IT {

    @Autowired
    PersonRepo personRepo;

    /**
     * <p>接口上标注的 @NotNull 等注解是生效的</p>
     * <p>接口上标注 @Validated，方法上标注 @NotNull</p>
     * <p>无需在实现类上添加注解</p>
     */
    @Test
    void givenNull_whenGetById_thenFail() {
        final Throwable throwable = catchThrowable(() -> personRepo.getById(null));
        then(throwable).isNotNull();
    }

    @Test
    void givenNullAddressProvince_whenValidateMethodObjectField_thenFail() {
        final Person person = new Person().setId(2L).setAddress(new Address());
        final Throwable throwable = catchThrowable(() -> personRepo.updatePerson(person));
        then(throwable).isNotNull();
    }

    @Test
    void given_whenValidateMethodReturnObject_thenFail() {
        final Throwable throwable = catchThrowable(() -> personRepo.getById(5L));
        then(throwable).isNotNull();
    }

    @Test
    void givenNullList_whenBatchGetById_thenFail() {
        Set<Long> idSet = Set.of(1L);
        Map<Long, String> idMap = Map.of(1L, "1");
        final Throwable throwable = catchThrowable(() -> personRepo.batchGetById(null, idSet, idSet, idSet, idMap, idMap));
        then(throwable).isNotNull();
    }

    @Test
    void givenEmptyList_whenBatchGetById_thenFail() {
        Set<Long> idSet = Set.of(1L);
        Map<Long, String> idMap = Map.of(1L, "1");
        final Throwable throwable = catchThrowable(() -> personRepo.batchGetById(new ArrayList<>(), idSet, idSet, idSet, idMap, idMap));
        then(throwable).isNotNull();
    }

    @Test
    void givenEmptySet_whenBatchGetById_thenFail() {
        Set<Long> idSet = Set.of(1L);
        Map<Long, String> idMap = Map.of(1L, "1");
        List<Long> idList = List.of(1L);
        Set<Long> nullIdSet = new HashSet<>() {{
            add(null);
        }};
        HashMap<Long, String> nullKeyMap = new HashMap<>() {{
            put(null, null);
        }};
        HashMap<Long, String> nullValueMap = new HashMap<>() {{
            put(1L, null);
        }};
        // idListSet0 可以为 null
        then(catchThrowable(() -> personRepo.batchGetById(idList, null, idSet, idSet, idMap, idMap))).isNull();
        //
        // idListSet 不可以为 null
        then(catchThrowable(() -> personRepo.batchGetById(idList, null, null, idSet, idMap, idMap))).isInstanceOf(ConstraintViolationException.class);
        // idListSet 不可以为 null, emptySet
        then(catchThrowable(() -> personRepo.batchGetById(idList, null, Set.of(), idSet, idMap, idMap))).isInstanceOf(ConstraintViolationException.class);
        // idListSet 不可以为 null, emptySet, 可以包含 null
        then(catchThrowable(() -> personRepo.batchGetById(idList, null, nullIdSet, idSet, idMap, idMap))).isNull();
        //
        // idListSet2 不可以为 null, emptySet, 不可以包含 null
        then(catchThrowable(() -> personRepo.batchGetById(idList, null, nullIdSet, nullIdSet, idMap, idMap))).isInstanceOf(ConstraintViolationException.class).isNotNull();
        //
        then(catchThrowable(() -> personRepo.batchGetById(idList, null, idSet, idSet, null, idMap))).isInstanceOf(ConstraintViolationException.class).isNotNull();
        // idListMap 不可以为 null, emptySet, key 不可以包含 null, value 可以包含 null
        then(catchThrowable(() -> personRepo.batchGetById(idList, null, idSet, idSet, null, idMap))).isInstanceOf(ConstraintViolationException.class).isNotNull();
        // idListMap 不可以为 null, emptySet, key 不可以包含 null, value 可以包含 null
        then(catchThrowable(() -> personRepo.batchGetById(idList, null, idSet, idSet, emptyMap(), idMap))).isInstanceOf(ConstraintViolationException.class).isNotNull();
        // idListMap 不可以为 null, emptySet, key 不可以包含 null, value 可以包含 null
        then(catchThrowable(() -> personRepo.batchGetById(idList, null, idSet, idSet, nullKeyMap, idMap))).isInstanceOf(ConstraintViolationException.class).isNotNull();
        // idListMap 不可以为 null, emptySet, key 不可以包含 null, value 可以包含 null
        then(catchThrowable(() -> personRepo.batchGetById(idList, null, idSet, idSet, nullValueMap, idMap))).isNull();
        //
        // idListMap2 不可以为 null, emptySet, key 不可以包含 null, value 不可以包含 null
        then(catchThrowable(() -> personRepo.batchGetById(idList, null, idSet, idSet, nullValueMap, nullValueMap))).isInstanceOf(ConstraintViolationException.class).isNotNull();
    }



}