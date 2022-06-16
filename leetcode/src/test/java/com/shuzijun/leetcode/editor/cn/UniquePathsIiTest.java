
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


class UniquePathsIiTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {

    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int uniquePathsWithObstacles(int[][] obstacleGrid) {
            int n = obstacleGrid[0].length;
            int[] dp = new int[n];
            dp[0] = 1;
            for (int i = 0; i < obstacleGrid.length; i++) {
                for (int j = 0; j < n; j++) {
                    if (obstacleGrid[i][j] == 1) {
                        dp[j] = 0;
                        continue;
                    }
                    dp[j] = (i == 0) ? ((j == 0) ? 1 : dp[j - 1])
                            : ((j == 0) ? dp[j] : dp[j - 1] + dp[j]);
                }
            }
            return dp[n - 1];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}