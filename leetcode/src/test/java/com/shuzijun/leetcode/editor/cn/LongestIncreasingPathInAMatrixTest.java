
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class LongestIncreasingPathInAMatrixTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.longestIncreasingPath(Utils.to2Array("[[3,4,5],[3,2,6],[2,2,1]]")))
                .isEqualTo(4);
        then(solution.longestIncreasingPath(Utils.to2Array("[[0],[1],[5],[5]]")))
                .isEqualTo(3);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        int[][] arrows = new int[][]{
                new int[]{0, -1},
                new int[]{-1, 0},
                new int[]{0, 1},
                new int[]{1, 0},
        };

        public int longestIncreasingPath(int[][] matrix) {
            final int row = matrix.length;
            final int column = matrix[0].length;
            int[] calc = new int[row * column];
            int[][] dp = new int[row + 1][column + 1];
            int ans = 1;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    if (calc[i * column + j] == 0) {
                        dfsCalc(matrix, i, j, dp, calc);
                    }
                    ans = Math.max(ans, dp[i][j]);
                }
            }
            return ans;
        }

        private void dfsCalc(int[][] matrix, int i, int j, int[][] dp, int[] calc) {
            final int row = matrix.length;
            final int column = matrix[0].length;
            if (calc[i * column + j] == 1) {
                return;
            }
            int maxAns = 1;
            for (int[] arrow : arrows) {
                int ni = i + arrow[0];
                int nj = j + arrow[1];
                if (ni >= 0 && ni < row
                        && nj >= 0 && nj < column
                        && matrix[i][j] < matrix[ni][nj]) {
                    dfsCalc(matrix, ni, nj, dp, calc);
                    maxAns = Math.max(maxAns, dp[ni][nj] + 1);
                }
            }
            dp[i][j] = maxAns;
            // 表示此节点已经计算过
            calc[i * column + j] = 1;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}