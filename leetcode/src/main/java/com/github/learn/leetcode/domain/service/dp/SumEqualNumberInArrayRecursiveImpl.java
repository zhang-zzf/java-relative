package com.github.learn.leetcode.domain.service.dp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/28
 */
@Service
@Validated
@Slf4j
public class SumEqualNumberInArrayRecursiveImpl implements SumEqualNumberInArray {

    @Override
    public boolean equal(int[] array, int sum) {
        return subSum(array, array.length, sum);
    }

    private boolean subSum(int[] array, int length, int sum) {
        if (length == 1) {
            return array[0] == sum;
        }
        if (sum == 0) {
            return true;
        }
        if (array[length - 1] > sum) {
            return false;
        }
        return subSum(array, length - 1, sum - array[length - 1])
            || subSum(array, length - 1, sum);
    }

}
