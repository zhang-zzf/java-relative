
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
            final int row = obstacleGrid.length;
            final int column = obstacleGrid[0].length;
            int[] prev = new int[column + 1];
            int[] cur = new int[column + 1];
            // init
            prev[1] = 1;
            for (int[] rowLine : obstacleGrid) {
                for (int j = 0; j < column; j++) {
                    cur[j + 1] = cur[j] + prev[j + 1];
                    if (rowLine[j] == 1) {
                        cur[j + 1] = 0;
                    }
                }
                int[] tmp = prev;
                prev = cur;
                cur = tmp;
            }
            return prev[column];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}