
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class MinimumPathSumTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int minPathSum = solution.minPathSum(new int[][]{
                new int[]{1, 3, 1},
                new int[]{1, 5, 1},
                new int[]{4, 2, 1}
        });
        then(minPathSum).isEqualTo(7);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int minPathSum(int[][] grid) {
            final int m = grid.length;
            final int n = grid[0].length;
            int[][] minSum = new int[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    minSum[i][j] = grid[i][j] + Math.min(
                            i > 0 ? minSum[i - 1][j] : Integer.MAX_VALUE,
                            j > 0 ? minSum[i][j - 1] : (i == 0 ? 0 : Integer.MAX_VALUE)
                    );
                }
            }
            return minSum[m - 1][n - 1];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}