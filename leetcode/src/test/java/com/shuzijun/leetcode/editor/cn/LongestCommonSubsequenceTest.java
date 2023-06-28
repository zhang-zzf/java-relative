package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class LongestCommonSubsequenceTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    then(solution.longestCommonSubsequence("abcde", "abc")).isEqualTo(3);
    then(solution.longestCommonSubsequence("abc", "abcde")).isEqualTo(3);
    then(solution.longestCommonSubsequence("abc", "def")).isEqualTo(0);
    then(solution.longestCommonSubsequence("ezupkr", "ubmrapg")).isEqualTo(2);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int longestCommonSubsequence(String text1, String text2) {
      final int cLng = text2.length();
      final int rLng = text1.length();
      int[][] dp = new int[rLng + 1][cLng + 1];
      for (int i = 0; i < rLng; i++) {
        final char c1 = text1.charAt(i);
        for (int j = 0; j < cLng; j++) {
          if (c1 == text2.charAt(j)) {
            dp[i + 1][j + 1] = dp[i][j] + 1;
          } else {
            dp[i + 1][j + 1] = Math.max(dp[i + 1][j], dp[i][j + 1]);
          }
        }
      }
      return dp[rLng][cLng];
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}