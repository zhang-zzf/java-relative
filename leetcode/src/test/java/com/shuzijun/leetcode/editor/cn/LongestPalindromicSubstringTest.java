//给你一个字符串 s，找到 s 中最长的回文子串。 
//
// 
//
// 示例 1： 
//
// 
//输入：s = "babad"
//输出："bab"
//解释："aba" 同样是符合题意的答案。
// 
//
// 示例 2： 
//
// 
//输入：s = "cbbd"
//输出："bb"
// 
//
// 示例 3： 
//
// 
//输入：s = "a"
//输出："a"
// 
//
// 示例 4： 
//
// 
//输入：s = "ac"
//输出："a"
// 
//
// 
//
// 提示： 
//
// 
// 1 <= s.length <= 1000 
// s 仅由数字和英文字母（大写和/或小写）组成 
// 
// Related Topics 字符串 动态规划 👍 4027 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class LongestPalindromicSubstringTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    then(solution.longestPalindrome("baba")).isEqualTo("bab");
    then(solution.longestPalindrome("cbbd")).isEqualTo("bb");
    then(solution.longestPalindrome("a")).isEqualTo("a");
    then(solution.longestPalindrome("ac")).isEqualTo("a");
    then(solution.longestPalindrome("aaaa")).isEqualTo("aaaa");
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public String longestPalindrome(String s) {
      final int lng = s.length();
      // dp[i][j] == 0 表示 S[i..j] 是回文串
      int[][] dp = new int[lng][lng];
      String ans = s.substring(0, 1);
      for (int j = 0; j < lng; j++) {
        for (int i = j - 1; i >= 0; i--) {
          boolean isPalindrome =
              s.charAt(i) == s.charAt(j) && (i == j - 1 || dp[i + 1][j - 1] == 0);
          if (!isPalindrome) {
            dp[i][j] = 1;
            continue;
          }
          if (j - i + 1 > ans.length()) {
            ans = s.substring(i, j + 1);
          }
        }
      }
      return ans;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}