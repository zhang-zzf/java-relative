// 给你一个按 非递减顺序 排序的整数数组 nums，返回 每个数字的平方 组成的新数组，要求也按 非递减顺序 排序。
//
// 
// 
//
// 
//
// 示例 1： 
//
// 
// 输入：nums = [-4,-1,0,3,10]
// 输出：[0,1,9,16,100]
// 解释：平方后，数组变为 [16,1,0,9,100]
// 排序后，数组变为 [0,1,9,16,100]
//
// 示例 2： 
//
// 
// 输入：nums = [-7,-3,2,3,11]
// 输出：[4,9,9,49,121]
// 
//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 10⁴ 
// -10⁴ <= nums[i] <= 10⁴ 
// nums 已按 非递减顺序 排序 
// 
//
// 
//
// 进阶： 
//
// 
// 请你设计时间复杂度为 O(n) 的算法解决本问题 
// 
//
// Related Topics 数组 双指针 排序 
// 👍 679 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class SquaresOfASortedArrayTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        int[] squares = solution.sortedSquares(new int[]{-4, -1, 0, 3, 10});
        then(squares).containsExactly(0, 1, 9, 16, 100);
    }

    @Test
    void givenFailedCase1_when_thenSuccess() {
        int[] squares = solution.sortedSquares(new int[]{1});
        then(squares).containsExactly(1);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int[] sortedSquares(int[] nums) {
            int[] ans = new int[nums.length];
            int left = 0, right = nums.length - 1;
            for (int i = nums.length - 1; i >= 0; i--) {
                int i1 = nums[left] * nums[left];
                int i2 = nums[right] * nums[right];
                if (i1 > i2) {
                    ans[i] = i1;
                    left += 1;
                }
                else {
                    ans[i] = i2;
                    right -= 1;
                }
            }
            return ans;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}