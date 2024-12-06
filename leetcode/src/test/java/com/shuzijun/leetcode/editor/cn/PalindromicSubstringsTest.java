package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class PalindromicSubstringsTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.countSubstrings("abc")).isEqualTo(3);
        then(solution.countSubstrings("aaa")).isEqualTo(6);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int countSubstrings(String s) {
            final int length = s.length();
            int[][] dp = new int[length][length];
            int echoCnt = 0;
            for (int right = 0; right < length; right++) {
                final char c1 = s.charAt(right);
                for (int left = right; left >= 0; left--) {
                    if ((right == left)
                        || (s.charAt(left) == c1 && (left == right - 1 || dp[left + 1][right - 1] == 0))) {
                        echoCnt += 1;
                    }
                    else {
                        dp[left][right] = 1;
                    }
                }
            }
            return echoCnt;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}