package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class TargetSumTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    then(solution.findTargetSumWays(new int[]{1, 1, 1, 1, 1}, 3)).isEqualTo(5);
    then(solution.findTargetSumWays(new int[]{1}, 2)).isEqualTo(0);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int findTargetSumWays(int[] nums, int target) {
      int sum = 0;
      for (int num : nums) {
        sum += num;
      }
      if (Math.abs(target) > sum || (sum + target) % 2 == 1) { // 注意边界值
        return 0;
      }
      // W 表示背包容量
      int W = (sum + target) / 2;
      int[] dp = new int[W + 1];
      dp[0] = 1;
      for (int num : nums) {
        for (int w = W; w >= num; w--) {
          dp[w] = dp[w] + dp[w - num];
        }
      }
      return dp[W];
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}