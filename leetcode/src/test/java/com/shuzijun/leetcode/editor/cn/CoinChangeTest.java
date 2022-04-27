
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


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
            for (int i = 1; i <= amount; i++) {
                int min = Integer.MAX_VALUE;
                for (int j = 0; j < coins.length; j++) {
                    int idx = i - coins[j];
                    if (idx < 0 || dp[idx] == -1) {
                        continue;
                    }
                    min = Math.min(min, dp[idx] + 1);
                }
                dp[i] = (min == Integer.MAX_VALUE ? -1 : min);
            }
            return dp[amount];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}