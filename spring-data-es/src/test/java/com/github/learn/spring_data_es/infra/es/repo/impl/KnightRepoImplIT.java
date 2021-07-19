package com.github.learn.spring_data_es.infra.es.repo.impl;

import static org.assertj.core.api.BDDAssertions.then;

import com.github.learn.spring_data_es.domain.model.Knight;
import com.github.learn.spring_data_es.domain.model.Knight.Account;
import com.github.learn.spring_data_es.domain.model.Knight.Identity;
import com.github.learn.spring_data_es.domain.model.Knight.Register;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.util.Lists;
import org.elasticsearch.common.util.set.Sets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhanfeng.zhang
 * @date 2021/06/04
 */
@SpringBootTest
class KnightRepoImplIT {

    @Autowired
    KnightRepoImpl knightRepo;

    final Long personId = 1L;
    final Long personId2 = 2L;

    @Test
    void testIndexDocument() {
        final Knight knight = buildKnight(personId);
        final Knight knight2 = buildKnight(personId2);
        knightRepo.index(Lists.newArrayList(knight, knight2));
        final List<Knight> knights = knightRepo.batchGetById(Arrays.asList(personId, personId2));
        then(knights).hasSize(2);
    }

    private Knight buildKnight(Long personId) {
        final Account crowd = new Account().setKnightId(personId).setSubType("众包")
            .setSubId(personId).setMobile("15618536513").setStatus("正常");
        final long fnId = personId + 1000000;
        final Account fn2 = new Account().setKnightId(fnId).setSubType("团队")
            .setSubId(fnId).setMobile("15618536513").setStatus("注销");
        final Identity identity = new Identity().setName("zhang.zzf").setSex("男").setEthnic("汉族").setCity("上海市")
            .setBirthDay(LocalDateTime.now());
        final Register register = new Register().setSubType("众包")
            .setCityId(1L)
            .setCreatedAt(LocalDateTime.now());
        final Knight knight = new Knight().setPersonId(personId)
            .setAccount(Sets.newHashSet(crowd, fn2))
            .setIdentity(identity)
            .setRegister(register);
        return knight;
    }

}