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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


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
            final int length = s.length();
            if (length < 2) {
                return s;
            }
            int maxLength = 1;
            int start = 0;
            int[][] dp = new int[length][length];
            for (int right = 1; right < length; right++) {
                for (int left = 0; left < right; left++) {
                    if (s.charAt(right) != s.charAt(left)) {
                        continue;
                    }
                    if (right - left <= 2) {
                        dp[left][right] = 1;
                    } else {
                        dp[left][right] = dp[left + 1][right - 1];
                    }
                    if (dp[left][right] == 1 && right - left + 1 > maxLength) {
                        start = left;
                        maxLength = right - left + 1;
                    }
                }
            }
            return s.substring(start, start + maxLength);
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}