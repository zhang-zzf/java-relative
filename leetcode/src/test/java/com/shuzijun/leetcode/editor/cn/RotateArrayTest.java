//给你一个数组，将数组中的元素向右轮转 k 个位置，其中 k 是非负数。 
//
// 
//
// 示例 1: 
//
// 
//输入: nums = [1,2,3,4,5,6,7], k = 3
//输出: [5,6,7,1,2,3,4]
//解释:
//向右轮转 1 步: [7,1,2,3,4,5,6]
//向右轮转 2 步: [6,7,1,2,3,4,5]
//向右轮转 3 步: [5,6,7,1,2,3,4]
// 
//
// 示例 2: 
//
// 
//输入：nums = [-1,-100,3,99], k = 2
//输出：[3,99,-1,-100]
//解释: 
//向右轮转 1 步: [99,-1,-100,3]
//向右轮转 2 步: [3,99,-1,-100] 
//
// 
//
// 提示： 
//
// 
// 1 <= nums.length <= 10⁵ 
// -2³¹ <= nums[i] <= 2³¹ - 1 
// 0 <= k <= 10⁵ 
// 
//
// 
//
// 进阶： 
//
// 
// 尽可能想出更多的解决方案，至少有 三种 不同的方法可以解决这个问题。 
// 你可以使用空间复杂度为 O(1) 的 原地 算法解决这个问题吗？ 
// 
//
// 
// 
//
// 
// 
//
// Related Topics 数组 数学 双指针 
// 👍 1669 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;
import org.junit.jupiter.api.Test;


public class RotateArrayTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    int[] nums = new int[]{1, 2, 3, 4, 5, 6, 7};
    int k = 3;
    int[] target = rotateForTest(nums, k);
    solution.rotate(nums, k);
    then(nums).containsExactly(target);
  }

  private int[] rotateForTest(int[] nums, int k) {
    int[] target = new int[nums.length];
    for (int i = 0; i < nums.length; i++) {
      // 把 nums[i] 直接复制到 target 数组
      target[(i + k) % nums.length] = nums[i];
    }
    return target;
  }

  @Test
  void givenOddNums_when_thenSuccess() {
    int[] nums = new int[]{1, 2, 3, 4, 5, 6, 7};
    for (int k = 0; k < nums.length; k++) {
      int[] copy = Arrays.copyOf(nums, nums.length);
      int[] target = rotateForTest(copy, k);
      solution.rotate(copy, k);
      then(copy).containsExactly(target);
    }
  }

  @Test
  void givenFailedCase1_when_then() {
    int[] nums = new int[]{-1, -100, 3, 99};
    int[] target = rotateForTest(nums, 2);
    solution.rotate(nums, 2);
    then(nums).containsExactly(target);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public void rotate(int[] nums, int k) {
      int lng = nums.length;
      if (lng == 0) {
        return;
      }
      k = k % lng;
      if (k == 0) {
        return;
      }
      int count = 0;
      for (int i = 0; i < lng; i++) {
        count += 1;
        for (int j = (i + k) % lng; j != i; j = (j + k) % lng) {
          int tmp = nums[i];
          nums[i] = nums[j];
          nums[j] = tmp;
          count += 1;
        }
        if (count == lng) {
          break;
        }
      }
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}