
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class LongestPalindromicSubsequenceTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int ans = solution.longestPalindromeSubseq("bbbab");
        then(ans).isEqualTo(4);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int longestPalindromeSubseq(String s) {
            final int lng = s.length();
            int[][] dp = new int[lng][lng];
            int ans = 1;
            for (int j = 0; j < lng; j++) {
                for (int i = j; i >= 0; i--) {
                    int maxLng;
                    if (i == j) {
                        maxLng = 1;
                    } else if (i == j - 1) {
                        maxLng = s.charAt(i) == s.charAt(j) ? 2 : 1;
                    } else {
                        maxLng = s.charAt(i) == s.charAt(j) ? dp[i + 1][j - 1] + 2
                                : Math.max(dp[i][j - 1], dp[i + 1][j]);
                    }
                    dp[i][j] = maxLng;
                    ans = Math.max(ans, maxLng);
                }
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}