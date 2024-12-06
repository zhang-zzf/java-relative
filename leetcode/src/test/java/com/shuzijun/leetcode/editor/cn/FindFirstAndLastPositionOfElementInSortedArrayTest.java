// 给定一个按照升序排列的整数数组 nums，和一个目标值 target。找出给定目标值在数组中的开始位置和结束位置。
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
// 输入：nums = [5,7,7,8,8,10], target = 8
// 输出：[3,4]
//
// 示例 2： 
//
// 
// 输入：nums = [5,7,7,8,8,10], target = 6
// 输出：[-1,-1]
//
// 示例 3： 
//
// 
// 输入：nums = [], target = 0
// 输出：[-1,-1]
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

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class FindFirstAndLastPositionOfElementInSortedArrayTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int[] range = solution.searchRange(new int[]{5, 7, 7, 8, 8, 8, 10, 10, 11}, 8);
        then(range).containsExactly(3, 5);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int[] searchRange(int[] nums, int target) {
            int[] ans = {-1, -1};
            if (nums.length == 0
                || target < nums[0]
                || target > nums[nums.length - 1]) {
                return ans;
            }
            int left = 0, right = nums.length - 1;
            int idx = -1;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                if (nums[mid] < target) {
                    left = mid + 1;
                }
                else {
                    right = mid - 1;
                    idx = mid;
                }
            }
            if (nums[idx] != target) {
                return ans;
            }
            ans[0] = idx;
            left = 0;
            right = nums.length - 1;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                if (nums[mid] <= target) {
                    left = mid + 1;
                    idx = mid;
                }
                else {
                    right = mid - 1;
                }
            }
            ans[1] = idx;
            return ans;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}