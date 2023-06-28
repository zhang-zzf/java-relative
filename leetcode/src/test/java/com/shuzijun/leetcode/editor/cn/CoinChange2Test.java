package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class CoinChange2Test {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final int change = solution.change(5, new int[]{1, 2, 5});
    then(change).isEqualTo(4);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int change(int amount, int[] coins) {
      int[] dp = new int[amount + 1];
      dp[0] = 1;
      for (int coin : coins) {
        for (int i = coin; i <= amount; i++) {
          dp[i] += dp[i - coin];
        }
      }
      return dp[amount];
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}