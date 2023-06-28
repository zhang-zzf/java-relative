package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class MaximalSquareTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final int ans = solution.maximalSquare(new char[][]{
        new char[]{'1', '0', '1', '0', '0'},
        new char[]{'1', '0', '1', '1', '1'},
        new char[]{'1', '1', '1', '1', '1'},
        new char[]{'1', '0', '0', '1', '0'},
    });
    then(ans).isEqualTo(4);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int maximalSquare(char[][] matrix) {
      int finalAnswer = 0;
      final int rowLng = matrix.length;
      final int columnLng = matrix[0].length;
      int[][] prefixSum = new int[rowLng + 1][columnLng + 1];
      for (int i = 0; i < rowLng; i++) {
        for (int j = 0; j < columnLng; j++) {
          prefixSum[i + 1][j + 1] = prefixSum[i][j + 1] + prefixSum[i + 1][j]
              - prefixSum[i][j]
              // char to number
              + (matrix[i][j] - 48);
        }
      }
      for (int i = 0; i < rowLng; i++) {
        for (int j = 0; j < columnLng; j++) {
          if (matrix[i][j] == '0') {
            continue;
          }
          finalAnswer = Math.max(finalAnswer, 1);
          int width = 1;
          while (i + width < rowLng && j + width < columnLng) {
            final int areaSum = getAreaSum(prefixSum, i, j, width);
            if (areaSum == (width + 1) * (width + 1)) {
              width += 1;
              finalAnswer = Math.max(finalAnswer, areaSum);
            } else {
              break;
            }
          }
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