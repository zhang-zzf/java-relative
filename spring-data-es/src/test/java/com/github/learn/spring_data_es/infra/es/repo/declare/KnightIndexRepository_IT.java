package com.github.learn.spring_data_es.infra.es.repo.declare;

import static org.assertj.core.api.BDDAssertions.then;

import com.github.learn.spring_data_es.infra.es.entity.Knight;
import com.github.learn.spring_data_es.infra.es.entity.Knight.Account;
import com.github.learn.spring_data_es.infra.es.entity.Knight.Identity;
import com.github.learn.spring_data_es.infra.es.entity.Knight.Register;
import com.google.common.collect.Sets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.TypedSort;

/**
 * @author zhanfeng.zhang
 * @date 2021/06/08
 */
@SpringBootTest
class KnightIndexRepository_IT {

    @Autowired
    KnightIndexRepository knightIndexRepository;

    @Test
    void testBasicUsage() {
        final long randomId = new Random().nextLong();
        // save
        final Knight saved = knightIndexRepository.save(buildKnight(randomId));
        // findById
        final Optional<Knight> byId = knightIndexRepository.findById(randomId);
        then(byId).isPresent();
        // count
        final long count = knightIndexRepository.count();
        then(count).isGreaterThan(0);
        // find all by page and sort
        final TypedSort<Knight> knightSort = Sort.sort(Knight.class);
        final Sort sort = knightSort.by((Knight k) -> k.getRegister().getCreatedAt()).descending()
            .and(knightSort.by(Knight::getPersonId).ascending());
        final Page<Knight> pageKnight = knightIndexRepository.findAll(PageRequest.of(0, 1, sort));
        then(pageKnight.getContent()).hasSize(1);
        // delete by id
        knightIndexRepository.deleteById(randomId);
        then(knightIndexRepository.findById(randomId)).isNotPresent();
    }

    @Test
    void testCustomRepositoryApi() {
        final Iterable<Knight> knights = knightIndexRepository.saveAll(Arrays.asList(buildKnight(1L)));
        final Optional<Knight> byAccount_knightId = knightIndexRepository.findByAccount_KnightId(1L);
        then(byAccount_knightId).isPresent();
        final List<Knight> byAccount_knightIdAndAccount_subId = knightIndexRepository
            .findByAccount_KnightIdAndAccount_SubId(1L, 1L);
        then(byAccount_knightIdAndAccount_subId).hasSize(1);
        then(knightIndexRepository.findByAccount_KnightIdAndAccount_SubId(1L, 2L)).hasSize(0);
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