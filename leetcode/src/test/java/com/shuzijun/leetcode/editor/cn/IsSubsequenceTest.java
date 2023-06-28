package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class IsSubsequenceTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    then(solution.isSubsequence("abc", "ahdbgdc")).isTrue();
    then(solution.isSubsequence("axc", "ahdbgdc")).isFalse();
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public boolean isSubsequence(String s, String t) {
      if (s.length() == 0) {
        return true;
      }
      if (t.length() == 0) {
        return false;
      }
      // 分析 t 字符串
      final int lng = t.length();
      // 构建分析数据
      Integer[][] dp = new Integer[lng][26];
      final char[] chars = t.toCharArray();
      for (int i = 0; i < lng; i++) {
        for (int j = i; j < lng; j++) {
          final int columnIdx = chars[j] - 'a';
          if (dp[i][columnIdx] == null) {
            dp[i][columnIdx] = j;
          }
        }
      }
      // 是有分析的数据在 O(1) 的时间复杂度内查询字符是否存在
      Integer targetIdx = 0;
      int sourceIdx = 0;
      while (sourceIdx < s.length()) {
        char c = s.charAt(sourceIdx);
        targetIdx = dp[targetIdx][c - 'a'];
        if (targetIdx == null) {
          break;
        }
        sourceIdx += 1;
        targetIdx += 1;
        if (targetIdx == lng) {
          break;
        }
      }
      return sourceIdx == s.length();
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}