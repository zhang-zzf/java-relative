//给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。 
//
// 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。 
//
// 
//
// 示例 1: 
//
// 
//输入: [3,2,1,5,6,4] 和 k = 2
//输出: 5
// 
//
// 示例 2: 
//
// 
//输入: [3,2,3,1,2,4,5,5,6] 和 k = 4
//输出: 4 
//
// 
//
// 提示： 
//
// 
// 1 <= k <= nums.length <= 10⁴ 
// -10⁴ <= nums[i] <= 10⁴ 
// 
// Related Topics 数组 分治 快速选择 排序 堆（优先队列） 👍 1485 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class KthLargestElementInAnArrayTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    then(solution.findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2)).isEqualTo(5);
    then(solution.findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4)).isEqualTo(4);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int findKthLargest(int[] nums, int k) {
      if (k > nums.length) {
        throw new IllegalArgumentException();
      }
      int left = 0, right = nums.length - 1;
      int targetIdx = nums.length - k;
      while (true) {
        int pivotIdx = partition(nums, left, right);
        if (pivotIdx == targetIdx) {
          return nums[pivotIdx];
        }
        if (pivotIdx < targetIdx) {
          left = pivotIdx + 1;
        } else {
          right = pivotIdx - 1;
        }
      }
    }

    private int partition(int[] nums, int left, int right) {
      int pivot = nums[right];
      // nums[left..j] < pivot
      int j = left - 1;
      for (int i = left; i < right; i++) {
        if (nums[i] < pivot) {
          swap(nums, ++j, i);
        }
      }
      swap(nums, j + 1, right);
      return j + 1;
    }

    private void swap(int[] nums, int i, int j) {
      int tmp = nums[i];
      nums[i] = nums[j];
      nums[j] = tmp;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}