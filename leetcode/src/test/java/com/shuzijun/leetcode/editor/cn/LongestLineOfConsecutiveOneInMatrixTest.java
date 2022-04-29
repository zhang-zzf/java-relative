
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class LongestLineOfConsecutiveOneInMatrixTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int ans = solution.longestLine(new int[][]{
                new int[]{0, 1, 1, 0},
                new int[]{0, 1, 1, 0},
                new int[]{0, 0, 1, 0},
        });
        then(ans).isEqualTo(3);
        final int ans2 = solution.longestLine(new int[][]{
                new int[]{1, 0, 1, 1, 0, 0, 1, 0, 0, 1},
                new int[]{0, 1, 1, 0, 1, 0, 1, 0, 1, 1},
                new int[]{0, 0, 1, 0, 1, 0, 0, 1, 0, 0},
                new int[]{1, 0, 1, 0, 1, 1, 1, 1, 1, 1},
                new int[]{0, 1, 0, 1, 1, 0, 0, 0, 0, 1},
                new int[]{0, 0, 1, 0, 1, 1, 1, 0, 1, 0},
                new int[]{0, 1, 0, 1, 0, 1, 0, 0, 1, 1},
                new int[]{1, 0, 0, 0, 1, 1, 1, 1, 0, 1},
                new int[]{1, 1, 1, 1, 1, 1, 1, 0, 1, 0},
                new int[]{1, 1, 1, 1, 0, 1, 0, 0, 1, 1}
        });
        then(ans2).isEqualTo(7);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        // 第 i 行的 dp 仅依赖 i-1 行的数据，可以简化为 O(n) 的空间复杂度
        public int longestLine(int[][] mat) {
            int ans = 0;
            final int row = mat.length;
            final int column = mat[0].length;
            int[][][] dp = new int[row + 1][column + 2][4];
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    if (mat[i][j] == 1) {
                        dp[i + 1][j + 1][0] = dp[i + 1][j][0] + 1;
                        ans = Math.max(dp[i + 1][j + 1][0], ans);
                        dp[i + 1][j + 1][1] = dp[i][j][1] + 1;
                        ans = Math.max(dp[i + 1][j + 1][1], ans);
                        dp[i + 1][j + 1][2] = dp[i][j + 1][2] + 1;
                        ans = Math.max(dp[i + 1][j + 1][2], ans);
                        dp[i + 1][j + 1][3] = dp[i][j + 2][3] + 1;
                        ans = Math.max(dp[i + 1][j + 1][3], ans);
                    }
                }
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}