package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class UniquePathsTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final int paths = solution.uniquePaths(3, 7);
    then(paths).isEqualTo(28);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int uniquePaths(int m, int n) {
      int[] dp = new int[n];
      for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
          dp[j] = (i == 0) ? 1 : ((j == 0) ? 1 : dp[j - 1] + dp[j]);
        }
      }
      return dp[n - 1];
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}