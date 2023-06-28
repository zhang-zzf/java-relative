package com.github.learn.leetcode.domain.service.union_array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class ArrayUnionServiceForceCrack implements ArrayUnionService {

  @Override
  public @NotNull int[] union(@NotNull int[] nums1, @NotNull int[] nums2) {
    Arrays.sort(nums1);
    Arrays.sort(nums2);
    List<Integer> ret = new ArrayList<>();
    for (int i = 0, j = 0; i < nums1.length && j < nums2.length; ) {
      if (nums1[i] < nums2[j]) {
        i += 1;
      } else if (nums1[i] > nums2[j]) {
        j += 1;
      } else {
        ret.add(nums1[i]);
        i += 1;
        j += 1;
      }
    }
    return ret.stream().mapToInt(Integer::intValue).toArray();
  }

}
