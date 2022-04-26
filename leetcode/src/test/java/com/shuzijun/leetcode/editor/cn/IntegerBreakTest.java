
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class IntegerBreakTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.integerBreak(10)).isEqualTo(36);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int integerBreak(int n) {
            int[] dp = new int[n + 1];
            dp[1] = 1;
            for (int i = 2; i <= n; i++) {
                int max = 0;
                for (int j = 1; j < i; j++) {
                    max = Math.max(max, j * Math.max(i - j, dp[i - j]));
                }
                dp[i] = max;
            }
            return dp[n];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}