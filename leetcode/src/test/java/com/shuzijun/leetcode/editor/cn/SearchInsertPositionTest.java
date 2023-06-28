//给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。 
//
// 请必须使用时间复杂度为 O(log n) 的算法。 
//
// 
//
// 示例 1: 
//
// 
//输入: nums = [1,3,5,6], target = 5
//输出: 2
// 
//
// 示例 2: 
//
// 
//输入: nums = [1,3,5,6], target = 2
//输出: 1
// 
//
// 示例 3: 
//
// 
//输入: nums = [1,3,5,6], target = 7
//输出: 4
// 
//
// 
//
// 提示: 
//
// 
// 1 <= nums.length <= 10⁴ 
// -10⁴ <= nums[i] <= 10⁴ 
// nums 为 无重复元素 的 升序 排列数组 
// -10⁴ <= target <= 10⁴ 
// 
//
// Related Topics 数组 二分查找 
// 👍 1817 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class SearchInsertPositionTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    int idx = solution.searchInsert(new int[]{1, 5, 8, 10}, 5);
    then(idx).isEqualTo(1);
  }

  @Test
  void givenSmallest_when_thenReturnZero() {
    int idx = solution.searchInsert(new int[]{1, 5, 8, 10}, 0);
    then(idx).isEqualTo(0);
  }

  @Test
  void givenLargest_when_thenReturnLength() {
    int idx = solution.searchInsert(new int[]{1, 5, 8, 10}, 11);
    then(idx).isEqualTo(4);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int searchInsert(int[] nums, int target) {
      // 值域 [0, nums.length]
      int ans = nums.length;
      int left = 0, right = nums.length - 1;
      while (left <= right) {
        int mid = left + ((right - left) >> 1);
        if (getV(nums, mid) >= target) {
          ans = mid;
          right = mid - 1;
        } else {
          left = mid + 1;
        }
      }
      return ans;
    }

    private int getV(int[] nums, int mid) {
      return nums[mid];
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}