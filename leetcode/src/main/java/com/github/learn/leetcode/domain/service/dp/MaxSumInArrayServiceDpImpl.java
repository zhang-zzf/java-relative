package com.github.learn.leetcode.domain.service.dp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/28
 */
@Service
@Slf4j
@Validated
public class MaxSumInArrayServiceDpImpl implements MaxSumInArrayService {

    @Override
    public int maxSum(@NotNull int[] array) {
        if (array.length == 1) {
            return array[0];
        }
        int[] maxSum = new int[array.length];
        maxSum[0] = array[0];
        maxSum[1] = Math.max(array[0], array[1]);
        for (int i = 2; i < array.length; i++) {
            int p1 = array[i] + maxSum[i - 2];
            int p2 = maxSum[i - 1];
            maxSum[i] = Math.max(p1, p2);
        }
        return maxSum[array.length - 1];
    }

}
