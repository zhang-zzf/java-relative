package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;


@Slf4j
class MinimumAsciiDeleteSumForTwoStringsTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        String str = "eat";
        for (int i = str.length() - 1; i >= 0; i--) {
            log.info("charAt: {}", str.charAt(i));
            log.info("codePoint: {}", str.codePointAt(i));
        }
        final int ans = solution.minimumDeleteSum("eat", "sea");
        then(ans).isEqualTo(231);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int minimumDeleteSum(String s1, String s2) {
            final int s1Lng = s1.length();
            final int s2Lng = s2.length();
            int[][] dp = new int[s1Lng + 1][s2Lng + 1];
            for (int i = 0; i < s1Lng; i++) {
                final char c1 = s1.charAt(i);
                for (int j = 0; j < s2Lng; j++) {
                    dp[i + 1][j + 1] = Math.max(dp[i][j + 1], dp[i + 1][j]);
                    final char c2 = s2.charAt(j);
                    if (c1 == c2) {
                        dp[i + 1][j + 1] = Math.max(dp[i + 1][j + 1], dp[i][j] + c2);
                    }
                }
            }
            int sum1 = 0, sum2 = 0;
            for (int i = 0; i < s1Lng; i++) {
                sum1 += s1.charAt(i);
            }
            for (int i = 0; i < s2Lng; i++) {
                sum2 += s2.charAt(i);
            }
            return (sum1 - dp[s1Lng][s2Lng]) + (sum2 - dp[s1Lng][s2Lng]);
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}