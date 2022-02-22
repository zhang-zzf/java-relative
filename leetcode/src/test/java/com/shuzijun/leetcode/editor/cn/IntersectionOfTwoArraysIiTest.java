//给你两个整数数组 nums1 和 nums2 ，请你以数组形式返回两数组的交集。返回结果中每个元素出现的次数，应与元素在两个数组中都出现的次数一致（如果出现
//次数不一致，则考虑取较小值）。可以不考虑输出结果的顺序。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums1 = [1,2,2,1], nums2 = [2,2]
//输出：[2,2]
// 
//
// 示例 2: 
//
// 
//输入：nums1 = [4,9,5], nums2 = [9,4,9,8,4]
//输出：[4,9] 
//
// 
//
// 提示： 
//
// 
// 1 <= nums1.length, nums2.length <= 1000 
// 0 <= nums1[i], nums2[i] <= 1000 
// 
//
// 
//
// 进阶： 
//
// 
// 如果给定的数组已经排好序呢？你将如何优化你的算法？ 
// 如果 nums1 的大小比 nums2 小，哪种方法更优？ 
// 如果 nums2 的元素存储在磁盘上，内存是有限的，并且你不能一次加载所有的元素到内存中，你该怎么办？ 
// 
// Related Topics 数组 哈希表 双指针 二分查找 排序 👍 658 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.BDDAssertions.then;


public class IntersectionOfTwoArraysIiTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.intersect(new int[]{1, 1, 2, 2, 2, 2}, new int[]{2, 2})).containsExactly(2, 2);
        then(solution.intersect(new int[]{4, 9, 5}, new int[]{9, 4, 9, 8, 4})).contains(4, 9);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int[] intersect(int[] nums1, int[] nums2) {
            List<Integer> ret = new LinkedList<>();
            final Map<Integer, Long> numToCount = Arrays.stream(nums1)
                    .boxed()
                    .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
            Arrays.sort(nums2);
            for (int i = 0; i < nums2.length; i++) {
                final int num = nums2[i];
                Long cnt = numToCount.get(num);
                if (cnt == null) {
                    continue;
                }
                ret.add(num);
                while ((i + 1) < nums2.length && nums2[i + 1] == num) {
                    if (--cnt > 0) {
                        ret.add(num);
                    }
                    i += 1;
                }
            }
            return ret.stream().mapToInt(Integer::intValue).toArray();
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}