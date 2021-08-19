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
public class ArrayUnionServiceForceCrack implements ArrayUnionService {

    @Override
    public @NotNull int[] union(@NotNull int[] a1, @NotNull int[] a2) {
        int[] small = a1, large = a2;
        if (a1.length > a2.length) {
            small = a2;
            large = a1;
        }
        Set<Integer> largeSet = Arrays.stream(large).boxed().collect(Collectors.toSet());
        int[] ret = Arrays.stream(small).filter(i -> largeSet.contains(i)).toArray();
        return ret;
    }

}
