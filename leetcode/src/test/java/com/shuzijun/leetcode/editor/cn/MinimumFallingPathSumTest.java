
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class MinimumFallingPathSumTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int sum = solution.minFallingPathSum(new int[][]{
                new int[]{2, 1, 3},
                new int[]{6, 5, 4},
                new int[]{7, 8, 9},
        });
        then(sum).isEqualTo(13);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int minFallingPathSum(int[][] matrix) {
            final int lng = matrix.length;
            int[] prev = new int[lng];
            int[] cur = new int[lng];
            for (int[] row : matrix) {
                for (int j = 0; j < lng; j++) {
                    int min = prev[j];
                    if (j - 1 >= 0) {
                        min = Math.min(prev[j - 1], min);
                    }
                    if (j + 1 < lng) {
                        min = Math.min(prev[j + 1], min);
                    }
                    cur[j] = row[j] + min;
                }
                // 滚动数组
                int[] tmp = prev;
                prev = cur;
                cur = tmp;
            }
            int ans = prev[0];
            for (int i = 0; i < lng; i++) {
                ans = Math.min(ans, prev[i]);
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}