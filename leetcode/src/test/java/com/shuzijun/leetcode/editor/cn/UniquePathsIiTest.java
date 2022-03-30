
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
            // m 行数， n 列数
            int m = obstacleGrid.length, n = obstacleGrid[0].length;
            int[] prev = new int[n];
            int[] cur = new int[n];
            // 初始化
            for (int i = 0; i < n; i++) {
                if (obstacleGrid[0][i] == 1) {
                    break;
                }
                prev[i] = 1;
            }
            for (int i = 1; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    cur[j] = (j == 0 ? (prev[j] == 0 ? 0 : 1) : cur[j - 1] + prev[j]);
                    if (obstacleGrid[i][j] == 1) {
                        cur[j] = 0;
                    }
                }
                // 滚动数组
                int[] tmp = prev;
                prev = cur;
                cur = tmp;
            }
            return prev[n - 1];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}