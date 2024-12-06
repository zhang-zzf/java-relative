package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;
import org.junit.jupiter.api.Test;


class PaintHouseTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int minCost = solution.minCost(new int[][]{
            new int[]{17, 2, 17},
            new int[]{16, 16, 5},
            new int[]{14, 3, 19}
        });
        then(minCost).isEqualTo(10);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int minCost(int[][] costs) {
            int[] prev = new int[3];
            int[] dp = new int[3];
            for (int[] cost : costs) {
                dp[0] = Math.min(prev[1], prev[2]) + cost[0];
                dp[1] = Math.min(prev[0], prev[2]) + cost[1];
                dp[2] = Math.min(prev[0], prev[1]) + cost[2];
                int[] tmp = prev;
                prev = dp;
                dp = tmp;
            }
            return Arrays.stream(prev).min().getAsInt();
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}