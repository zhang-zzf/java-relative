
package com.shuzijun.leetcode.editor.cn;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class TossStrangeCoinsTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final double ans = solution.probabilityOfHeads(new double[]{0.5, 0.5, 0.5, 0.5, 0.5}, 0);
        then(ans).isCloseTo(0.03125, Offset.offset(0.00001));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public double probabilityOfHeads(double[] prob, int target) {
            double[] prev = new double[target + 1];
            double[] dp = new double[target + 1];
            prev[0] = 1d;
            for (double d : prob) {
                dp[0] = prev[0] * (1 - d);
                for (int i = 1; i <= target; i++) {
                    dp[i] = prev[i] * (1 - d) + prev[i - 1] * d;
                }
                // 滚动数组
                double[] tmp = prev;
                prev = dp;
                dp = tmp;
            }
            return prev[target];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}