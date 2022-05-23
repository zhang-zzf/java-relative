
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
            for (int w = 2; w <= n; w++) {
                dp[w] = w;
                for (int i = 2; i * i <= w; i++) {
                    if (w % i == 0) {
                        dp[w] = Math.min(dp[w], dp[i] + w / i);
                        dp[w] = Math.min(dp[w], dp[w / i] + i);
                    }
                }
            }
            return dp[n];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}