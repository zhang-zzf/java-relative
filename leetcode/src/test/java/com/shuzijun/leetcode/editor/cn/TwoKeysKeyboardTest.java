
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class TwoKeysKeyboardTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.minSteps(3)).isEqualTo(3);
        then(solution.minSteps(4)).isEqualTo(4);
        then(solution.minSteps(9)).isEqualTo(6);
        then(solution.minSteps(14)).isEqualTo(9);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int minSteps(int n) {
            int[] dp = new int[n + 1];
            for (int i = 2; i <= n; i++) {
                dp[i] = i;
                for (int j = 2; j * j <= i; j++) {
                    if (i % j == 0) {
                        dp[i] = Math.min(dp[i], dp[j] + i / j);
                        dp[i] = Math.min(dp[i], dp[i / j] + j);
                    }
                }
            }
            return dp[n];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}