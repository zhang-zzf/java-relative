
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class PerfectSquaresTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.numSquares(1)).isEqualTo(1);
        then(solution.numSquares(2)).isEqualTo(2);
        then(solution.numSquares(3)).isEqualTo(3);
        then(solution.numSquares(4)).isEqualTo(1);
        then(solution.numSquares(12)).isEqualTo(3);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int numSquares(int n) {
            int[] dp = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                dp[i] = i;
                for (int j = 1; (j * j) <= i; j++) {
                    dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
                }
            }
            return dp[n];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}