package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class OutOfBoundaryPathsTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    then(solution.findPaths(2, 2, 2, 0, 0)).isEqualTo(6);
    then(solution.findPaths(1, 3, 3, 0, 1)).isEqualTo(12);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int findPaths(int m, int n, int maxMove, int startRow, int startColumn) {
      int ans = 0;
      int MOD = 1000000007;
      int[][][] dp = new int[maxMove + 1][m][n];
      dp[0][startRow][startColumn] = 1;
      int[][] arrows = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};
      for (int i = 0; i < maxMove; i++) {
        for (int j = 0; j < m; j++) {
          for (int k = 0; k < n; k++) {
            final int pathCount = dp[i][j][k];
            if (pathCount > 0) {
              for (int[] arrow : arrows) {
                int nj = j + arrow[0];
                int nk = k + arrow[1];
                if (nj >= 0 && nj < m
                    && nk >= 0 && nk < n) {
                  dp[i + 1][nj][nk] = (dp[i + 1][nj][nk] + pathCount) % MOD;
                } else {
                  ans = (ans + pathCount) % MOD;
                }
              }
            }
          }
        }
      }
      return ans;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}