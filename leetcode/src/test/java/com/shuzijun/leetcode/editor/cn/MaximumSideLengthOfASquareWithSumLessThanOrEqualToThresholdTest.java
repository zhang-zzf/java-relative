
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class MaximumSideLengthOfASquareWithSumLessThanOrEqualToThresholdTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int ans = solution.maxSideLength(new int[][]{
                new int[]{1, 1, 3, 2, 4, 3, 2},
                new int[]{1, 1, 3, 2, 4, 3, 2},
                new int[]{1, 1, 3, 2, 4, 3, 2}
        }, 4);
        then(ans).isEqualTo(2);
    }


    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxSideLength(int[][] mat, int threshold) {
            int finalAnswer = 0;
            final int rowLng = mat.length;
            final int columnLng = mat[0].length;
            int[][] prefixSum = new int[rowLng + 1][columnLng + 1];
            for (int i = 0; i < rowLng; i++) {
                for (int j = 0; j < columnLng; j++) {
                    prefixSum[i + 1][j + 1] = prefixSum[i][j + 1] + prefixSum[i + 1][j]
                            - prefixSum[i][j]
                            + mat[i][j];
                }
            }
            for (int i = 0; i < rowLng; i++) {
                for (int j = 0; j < columnLng; j++) {
                    int ans = 0;
                    while (i + ans < rowLng && j + ans < columnLng) {
                        final int areaSum = getAreaSum(prefixSum, i, j, ans);
                        if (areaSum <= threshold) {
                            ans += 1;
                        } else {
                            break;
                        }
                    }
                    finalAnswer = Math.max(finalAnswer, ans);
                }
            }
            return finalAnswer;
        }

        private int getAreaSum(int[][] prefixSum, int i, int j, int ans) {
            int sr = i + 1, sc = j + 1, dr = i + 1 + ans, dc = j + 1 + ans;
            return prefixSum[dr][dc]
                    - prefixSum[sr - 1][dc] - prefixSum[dr][sc - 1]
                    + prefixSum[sr - 1][sc - 1];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}