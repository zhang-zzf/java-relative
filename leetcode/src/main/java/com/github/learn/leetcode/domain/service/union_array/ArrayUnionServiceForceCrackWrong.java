package com.github.learn.leetcode.domain.service.union_array;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/19
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Validated
public class ArrayUnionServiceForceCrackWrong implements ArrayUnionService {

    /**
     * 错误算法
     *
     * @param num1
     * @param num2
     * @return
     */
    @Override
    public @NotNull int[] union(@NotNull int[] num1, @NotNull int[] num2) {
        int[] small = num1, large = num2;
        if (num1.length > num2.length) {
            small = num2;
            large = num1;
        }
        Set<Integer> largeSet = Arrays.stream(large).boxed().collect(Collectors.toSet());
        int[] ret = Arrays.stream(small).filter(i -> largeSet.contains(i)).toArray();
        return ret;
    }

}
