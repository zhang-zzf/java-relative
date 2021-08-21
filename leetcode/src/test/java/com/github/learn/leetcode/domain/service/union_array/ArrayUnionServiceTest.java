package com.github.learn.leetcode.domain.service.union_array;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/19
 */
@SpringBootTest
class ArrayUnionServiceTest {

    @Autowired
    @Qualifier("arrayUnionServiceForceCrack")
    ArrayUnionService service;

    /**
     * 输入：nums1 = [1,2,2,1], nums2 = [2,2]
     * 输出：[2,2]
     */
    @Test
    void givenCaseOne_whenUnit_thenSuccess() {
        int[] result = service.union(new int[]{1, 2, 2, 1}, new int[]{2, 2});
        then(result).hasSize(2).contains(2);
    }

    /**
     * 输入：nums1 = [4,9,5], nums2 = [9,4,9,8,4]
     * 输出：[4,9]
     */
    @Test
    void givenCaseTwo_whenUnit_thenSuccess() {
        int[] result = service.union(new int[]{4, 9, 5}, new int[]{9, 4, 9, 8, 4});
        then(result).hasSize(2).contains(4, 9);
    }

    /**
     * 输入：
     * [3,1,2]
     * [1,1]
     * 预期结果：
     * [1]
     */
    @Test
    void givenCaseThree_whenUnit_thenSuccess() {
        int[] result = service.union(new int[]{3, 1, 2}, new int[]{1, 1});
        then(result).hasSize(1).contains(1);
    }

}