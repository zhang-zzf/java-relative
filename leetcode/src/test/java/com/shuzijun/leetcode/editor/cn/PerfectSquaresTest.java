
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
                int min = Integer.MAX_VALUE;
                for (int j = 1; j < i; j++) {
                    final int tmp = j * j;
                    if (tmp > i) {
                        break;
                    }
                    min = Math.min(min, dp[i - tmp]);
                }
                dp[i] = (min == Integer.MAX_VALUE ? 0 : min) + 1;
            }
            return dp[n];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}