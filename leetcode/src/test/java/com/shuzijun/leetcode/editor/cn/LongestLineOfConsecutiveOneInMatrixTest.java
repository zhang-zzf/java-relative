package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;
import org.junit.jupiter.api.Test;


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
        // failed case 1
        final int ans3 = solution.longestLine(new int[][]{
            new int[]{0, 1, 1, 0},
            new int[]{0, 1, 1, 0},
            new int[]{0, 0, 0, 1},
        });
        then(ans3).isEqualTo(3);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        // 第 i 行的 dp 仅依赖 i-1 行的数据，可以简化为 O(n) 的空间复杂度
        public int longestLine(int[][] mat) {
            int ans = 0;
            final int column = mat[0].length;
            int[][] dp = new int[column + 2][4];
            int[][] cur = new int[column + 2][4];
            for (int[] row : mat) {
                for (int c = 0; c < column; c++) {
                    final boolean isNumberOne = row[c] == 1;
                    cur[c + 1][0] = isNumberOne ? (cur[c][0] + 1) : 0;
                    cur[c + 1][1] = isNumberOne ? dp[c + 1][1] + 1 : 0;
                    cur[c + 1][2] = isNumberOne ? dp[c][2] + 1 : 0;
                    cur[c + 1][3] = isNumberOne ? dp[c + 2][3] + 1 : 0;
                    ans = Math.max(Arrays.stream(cur[c + 1]).max().orElse(0), ans);
                }
                // 滚动数组
                int[][] tmp = dp;
                dp = cur;
                cur = tmp;
            }
            return ans;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}