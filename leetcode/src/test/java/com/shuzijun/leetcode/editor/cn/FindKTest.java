package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

public class FindKTest {


  @Test
  void given_when_then() {
    int[] nums = {1, 2, 3, 3, 4, 4, 4, 4, 4, 5, 5, 8, 10, 12};
    Solution solution = new Solution();
    then(solution.findK(nums, 1)).isEqualTo(1);
    then(solution.findK(nums, 4)).isEqualTo(5);
    then(solution.findK(nums, 5)).isEqualTo(2);
    then(solution.findK(nums, 15)).isEqualTo(0);
    then(solution.findK(nums, -1)).isEqualTo(0);
  }


  @Test
  void given_whenFindLeft_then() {
    int[] nums = {1, 2, 3, 3, 4, 4, 4, 4, 4, 5, 5, 8, 10, 12};
    Solution solution = new Solution();
    then(solution.findLeft(nums, -1)).isEqualTo(-1);
    then(solution.findLeft(nums, 13)).isEqualTo(-1);
    then(solution.findLeft(nums, 1)).isEqualTo(0);
    then(solution.findLeft(nums, 2)).isEqualTo(1);
    then(solution.findLeft(nums, 12)).isEqualTo(nums.length - 1);
    then(solution.findLeft(nums, 10)).isEqualTo(nums.length - 2);
    then(solution.findLeft(nums, 4)).isEqualTo(4);
  }

  @Test
  void given_whenFindright_then() {
    int[] nums = {1, 2, 3, 3, 4, 4, 4, 4, 4, 5, 5, 8, 10, 12};
    Solution solution = new Solution();
    then(solution.findRight(nums, -1)).isEqualTo(-1);
    then(solution.findRight(nums, 13)).isEqualTo(-1);
    then(solution.findRight(nums, 1)).isEqualTo(0);
    then(solution.findRight(nums, 2)).isEqualTo(1);
    then(solution.findRight(nums, 12)).isEqualTo(nums.length - 1);
    then(solution.findRight(nums, 10)).isEqualTo(nums.length - 2);
    then(solution.findRight(nums, 4)).isEqualTo(8);
  }

  /**
   * https://t.zijieimg.com/rRsSkeE/ 找K的个数
   * <p>
   * 题目描述 输入：​ 一个递增数组，以及一个目标值K​
   * <p>
   * 输出：​ K的个数​
   * <p>
   * 要求时间复杂度 logN​
   * <p>
   * 测试用例：​ 数组：1, 2, 3, 3, 4, 4, 4, 4, 4, 5, 5, 8, 10, 12​ 当K为1，4，6，12时的个数
   */

  class Solution {

    /**
     * 找数组中最后一个等于 k 的元素的下标
     * <p>数组中不存在k返回-1</p>
     */
    int findRight(int[] nums, int k) {
      int left = 0, right = nums.length - 1;
      while (left < right) {
        int mid = left + ((right - left) >> 1) + 1; // 上取整
        if (nums[mid] <= k) {
          left = mid;
        } else {
          right = mid - 1;
        }
      }
      // left == right
      return nums[left] == k ? left : -1;
    }

    /**
     * 找数组中第一个等于 k 的元素的下标
     * <p>数组中不存在k返回-1</p>
     */
    int findLeft(int[] nums, int k) {
      int left = 0, right = nums.length - 1;
      while (left < right) {
        int mid = left + ((right - left) >> 1); // 向下取整
        if (nums[mid] >= k) {
          right = mid;
        } else {
          left = mid + 1;
        }
      }
      // left == right
      return nums[left] == k ? left : -1;
    }

    int findK(int[] nums, int k) {
      int left = 0, right = nums.length - 1;
      // 先找第一个k
      while (left < right) {
        int mid = left + ((right - left) >> 1);
        if (nums[mid] >= k) {
          right = mid;
        } else {
          left = mid + 1;
        }
      }
      int ans = 0;
      if (left >= nums.length) {
        return ans;
      }
      int firstKIdx = left;
      // 找最后一个k
      left = 0;
      right = nums.length - 1;
      while (left < right) {
        int mid = left + ((right - left) >> 1) + 1;
        if (nums[mid] > k) {
          right = mid - 1;
        } else {
          left = mid;
        }
      }
      return left - firstKIdx + 1;
    }

  }

}
