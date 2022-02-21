//给定一个按照升序排列的整数数组 nums，和一个目标值 target。找出给定目标值在数组中的开始位置和结束位置。 
//
// 如果数组中不存在目标值 target，返回 [-1, -1]。 
//
// 进阶： 
//
// 
// 你可以设计并实现时间复杂度为 O(log n) 的算法解决此问题吗？ 
// 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [5,7,7,8,8,10], target = 8
//输出：[3,4] 
//
// 示例 2： 
//
// 
//输入：nums = [5,7,7,8,8,10], target = 6
//输出：[-1,-1] 
//
// 示例 3： 
//
// 
//输入：nums = [], target = 0
//输出：[-1,-1] 
//
// 
//
// 提示： 
//
// 
// 0 <= nums.length <= 10⁵ 
// -10⁹ <= nums[i] <= 10⁹ 
// nums 是一个非递减数组 
// -10⁹ <= target <= 10⁹ 
// 
// Related Topics 数组 二分查找 👍 1458 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class FindFirstAndLastPositionOfElementInSortedArrayTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int[] range = solution.searchRange(new int[]{5, 7, 7, 8, 8, 8, 10, 10, 11}, 8);
        then(range).containsExactly(3, 5);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        /**
         * 为什么第一次是向下取整，第二次必须是向上取整
         * <p>left = right-1 时 int mid = left + ((right - left) >> 1) 的值一定等于 left</p>
         * <p>若第二次不想上取整，有可能死循环</p>
         */
        public int[] searchRange(int[] nums, int target) {
            final int[] ans = {-1, -1};
            if (nums.length == 0) {
                return ans;
            }
            int left = 0, right = nums.length - 1;
            while (left < right) {
                // 默认向下取整
                int mid = left + ((right - left) >> 1);
                if (nums[mid] >= target) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            if (nums[left] == target) {
                ans[0] = left;
            } else {
                return ans;
            }
            left = 0;
            right = nums.length - 1;
            while (left < right) {
                // 核心点：向上取整
                int mid = left + ((right - left) >> 1) + 1;
                if (nums[mid] <= target) {
                    left = mid;
                } else {
                    right = mid - 1;
                }
            }
            if (nums[left] == target) {
                ans[1] = left;
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}