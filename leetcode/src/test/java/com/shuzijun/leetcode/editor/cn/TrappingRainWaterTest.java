//给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。 
//
// 
//
// 示例 1： 
//
// 
//
// 
//输入：height = [0,1,0,2,1,0,1,3,2,1,2,1]
//输出：6
//解释：上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。 
// 
//
// 示例 2： 
//
// 
//输入：height = [4,2,0,3,2,5]
//输出：9
// 
//
// 
//
// 提示： 
//
// 
// n == height.length 
// 1 <= n <= 2 * 10⁴ 
// 0 <= height[i] <= 10⁵ 
// 
// Related Topics 栈 数组 双指针 动态规划 单调栈 👍 3046 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class TrappingRainWaterTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    then(solution.trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1})).isEqualTo(6);
    // failed case 1
    then(solution.trap(new int[]{4, 2, 0, 3, 2, 5})).isEqualTo(9);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int trap(int[] height) {
      int ans = 0;
      int left = 0, right = height.length - 1;
      int leftMax = 0, rightMax = 0;
      while (left < right) {
        leftMax = Math.max(leftMax, height[left]);
        rightMax = Math.max(rightMax, height[right]);
        if (height[left] < height[right]) {
          // 表示 left 右边有比它高的柱子且右边已知柱子的最大高度 rightMax > leftMax
          ans += leftMax - height[left];
          left += 1;
        } else {
          ans += rightMax - height[right];
          right -= 1;
        }
      }
      return ans;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}