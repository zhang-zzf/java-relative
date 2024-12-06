package com.github.learn.leetcode.domain.service.union_array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/19
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Validated
public class ArrayUnionServiceHashCrash implements ArrayUnionService {

    @Override
    public @NotNull int[] union(@NotNull int[] nums1, @NotNull int[] nums2) {
        Map<Integer, Integer> numToCnt = new HashMap<>(nums1.length);
        for (int i : nums1) {
            numToCnt.put(i, numToCnt.getOrDefault(i, 0) + 1);
        }
        Map<Integer, Integer> ret = new HashMap<>(4);
        for (int i : nums2) {
            if (numToCnt.containsKey(i)) {
                Integer cnt = ret.getOrDefault(i, 0);
                if (cnt < numToCnt.get(i)) {
                    cnt += 1;
                }
                ret.put(i, cnt);
            }
        }
        List<Integer> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> e : ret.entrySet()) {
            for (Integer i = 0; i < e.getValue(); i++) {
                list.add(e.getKey());
            }
        }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

}
