package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;


class TossStrangeCoinsTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final double ans = solution.probabilityOfHeads(new double[]{0.5, 0.5, 0.5, 0.5, 0.5}, 0);
        then(ans).isCloseTo(0.03125, Offset.offset(0.00001));
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public double probabilityOfHeads(double[] prob, int target) {
            double[] dp = new double[target + 1];
            dp[0] = 1d;
            for (double d : prob) {
                for (int w = target; w > 0; w--) {
                    dp[w] = dp[w] * (1 - d) + dp[w - 1] * d;
                }
                dp[0] *= (1 - d);
            }
            return dp[target];
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}