
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class PaintHouseIiTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int minCostII = solution.minCostII(new int[][]{
                new int[]{1, 5, 3},
                new int[]{2, 9, 4}
        });
        then(minCostII).isEqualTo(5);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int minCostII(int[][] costs) {
            int colorNum = costs[0].length;
            int[] prev = new int[colorNum];
            int[] dp = new int[colorNum];
            int min1Color = 0, min2Color = 0;
            for (int[] cost : costs) {
                for (int i = 0; i < colorNum; i++) {
                    if (i != min1Color) {
                        dp[i] = cost[i] + prev[min1Color];
                    } else {
                        dp[i] = cost[i] + prev[min2Color];
                    }
                }
                // 重新计算第一小的数
                int m1 = dp[0];
                min1Color = 0;
                for (int i = 0; i < colorNum; i++) {
                    if (dp[i] < m1) {
                        m1 = dp[i];
                        min1Color = i;
                    }
                }
                // 求第二小的数
                int m2 = Integer.MAX_VALUE;
                for (int i = 0; i < colorNum; i++) {
                    if (i == min1Color) {
                        continue;
                    }
                    if (dp[i] <= m2) {
                        m2 = dp[i];
                        min2Color = i;
                    }
                }
                // 滚动数组
                int[] tmp = prev;
                prev = dp;
                dp = tmp;
            }
            return prev[min1Color];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}