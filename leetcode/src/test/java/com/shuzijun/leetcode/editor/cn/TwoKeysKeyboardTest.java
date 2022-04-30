
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
            if (n == 1) {
                return 0;
            }
            int[] dp = new int[n + 1];
            dp[2] = 2;
            for (int i = 3; i <= n; i++) {
                for (int j = i - 1; j > 1; j--) {
                    if (i % j == 0) {
                        dp[i] = dp[j] + 1 + (i - j) / j;
                        break;
                    }
                }
                if (dp[i] == 0) {
                    dp[i] = i;
                }
            }
            return dp[n];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}