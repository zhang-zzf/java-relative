package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


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

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int numSquares(int n) {
            int[] dp = new int[n + 1];
            for (int w = 1; w <= n; w++) {
                dp[w] = w;
                for (int i = 1; (i * i) <= w; i++) {
                    dp[w] = Math.min(dp[w], dp[w - i * i] + 1);
                }
            }
            return dp[n];
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}