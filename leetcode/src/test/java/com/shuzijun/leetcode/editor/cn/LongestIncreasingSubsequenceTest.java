//给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。 
//
// 子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。例如，[3,6,2,7] 是数组 [0,3,1,6,2,2,7] 的子序
//列。 
// 
//
// 示例 1： 
//
// 
//输入：nums = [10,9,2,5,3,7,101,18]
//输出：4
//解释：最长递增子序列是 [2,3,7,101]，因此长度为 4 。
// 
//
// 示例 2： 
//
// 
//输入：nums = [0,1,0,3,2,3]
//输出：4
// 
//
// 示例 3： 
//
// 
//输入：nums = [7,7,7,7,7,7,7]
//输出：1
// 
//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 2500
// -10⁴ <= nums[i] <= 10⁴
// 
//
// 
//
// 进阶： 
//
// 
// 你可以设计时间复杂度为 O(n²) 的解决方案吗？ 
// 你能将算法的时间复杂度降低到 O(n log(n)) 吗? 
// 
// Related Topics 数组 二分查找 动态规划 👍 1879 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class LongestIncreasingSubsequenceTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18})).isEqualTo(4);
        then(solution.lengthOfLIS(new int[]{4, 10, 4, 3, 8, 9})).isEqualTo(3);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int lengthOfLIS(int[] nums) {
            int[] end = new int[nums.length];
            end[0] = 1;
            for (int i = 1; i < nums.length; i++) {
                int subMax = 0;
                for (int j = 0; j < i; j++) {
                    if (nums[j] < nums[i]) {
                        subMax = Math.max(subMax, end[j]);
                    }
                }
                end[i] = subMax + 1;
            }
            int max = 1;
            for (int i = 0; i < nums.length; i++) {
                max = Math.max(max, end[i]);
            }
            return max;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}