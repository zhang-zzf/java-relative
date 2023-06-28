package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class CoinChangeTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final int ans = solution.coinChange(new int[]{1, 2, 5}, 11);
    then(ans).isEqualTo(3);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int coinChange(int[] coins, int amount) {
      int[] dp = new int[amount + 1];
      for (int coin : coins) {
        for (int i = coin; i <= amount; i++) {
          if (i == coin) {
            dp[i] = 1;
            continue;
          }
          if (dp[i] == 0) {
            if (dp[i - coin] != 0) {
              dp[i] = dp[i - coin] + 1;
            }
          } else {
            if (dp[i - coin] == 0) {
              dp[i] = dp[i] + 1;
            } else {
              dp[i] = Math.min(dp[i], dp[i - coin]) + 1;
            }
          }
        }
      }
      return dp[amount] == 0 ? -1 : dp[amount];
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}