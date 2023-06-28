package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


class MatrixBlockSumTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {

  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int[][] matrixBlockSum(int[][] mat, int k) {
      final int rLng = mat.length;
      final int cLng = mat[0].length;
      int[][] ans = new int[rLng][cLng];
      // build prefixSum;
      int[][] prefixSum = new int[rLng + 1][cLng + 1];
      for (int i = 0; i < rLng; i++) {
        for (int j = 0; j < cLng; j++) {
          prefixSum[i + 1][j + 1] = prefixSum[i][j + 1] + prefixSum[i + 1][j]
              - prefixSum[i][j]
              + mat[i][j];
        }
      }
      for (int i = 0; i < rLng; i++) {
        for (int j = 0; j < cLng; j++) {
          ans[i][j] = calcArea(prefixSum,
              Math.max(0, i - k), Math.max(0, j - k),
              Math.min(rLng - 1, i + k), Math.min(cLng - 1, j + k));
        }
      }
      return ans;
    }

    private int calcArea(int[][] prefixSum, int x1, int y1, int x2, int y2) {
      return prefixSum[x2 + 1][y2 + 1]
          - prefixSum[x1][y2 + 1]
          - prefixSum[x2 + 1][y1]
          + prefixSum[x1][y1];
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}