package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class ByteDance3Test {


    /**
     * 一般场景测试
     */
    @Test
    void givenArray_whenCalcTarget_then() {
        Solution solution = new Solution();
        int ans = solution.partition(new int[]{3, 2, 4, 3, 6});
        then(ans).isEqualTo(3);
    }

    /**
     * 一般场景测试
     */
    @Test
    void givenArray2_whenCalcTarget_then() {
        Solution solution = new Solution();
        int ans = solution.partition(new int[]{1, 2, 3, 4, 5, 6, 7, 8});
        then(ans).isEqualTo(3);
    }

    /**
     * 测试极限场景
     */
    @Test
    void givenSameItemArray_whenCalcTarget_then() {
        Solution solution = new Solution();
        int ans = solution.partition(new int[]{2, 2, 2, 2, 2});
        new ArrayList<>().stream().distinct().collect(Collectors.toList());
        then(ans).isEqualTo(5);
    }

    /**
     * 测试极限场景
     */
    @Test
    void givenTwoItemArray_whenCalcTarget_then() {
        Solution solution = new Solution();
        int ans = solution.partition(new int[]{1, 2});
        then(ans).isEqualTo(1);
    }


    class Solution {

        /**
         * 给定数组 {3,2,4,3,6} 分成 m 份，每份和相等 求 m 的最大值
         */
        int partition(int[] nums) {
            // 数组和
            int sum = 0;
            for (int num : nums) {
                sum += num;
            }
            // 尝试 2 份到 length 份，由于求最大值，倒序遍历
            for (int m = nums.length; m > 1; m--) {
                // 拆分成 m 份
                if (sum % m != 0) {
                    continue;
                }
                // 可以拆解成 m 份，每份子序列的和为 sum/m
                int subSum = sum / m;
                if (calcSubSumNum(nums, subSum) == m) {
                    return m;
                }
            }
            // 默认 1
            return 1;
        }

        /**
         * 计算 nums 中和为 target 的子序列的个数
         * <p>限制：数组中的元素分配到一个子序列中</p>
         */
        int calcSubSumNum(int[] nums, int target) {
            int ans = 0;
            int length = nums.length;
            List<Integer> existsCombination = new ArrayList<>();
            for (int i = 1; i < (1 << length); i++) {
                final int curC = i;
                if (existsCombination.stream().anyMatch(combination -> (curC & combination) > 0)) {
                    // 数组元素已经被使用过
                    continue;
                }
                int sum = 0;
                for (int j = 0; j < length; j++) {
                    if ((i & 1 << j) != 0) {
                        sum += nums[j];
                    }
                }
                if (sum == target) {
                    ans += 1;
                    existsCombination.add(curC);
                }
            }
            return ans;
        }

    }

}
