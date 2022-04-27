
package com.shuzijun.leetcode.editor.cn;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


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

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int minimumDeleteSum(String s1, String s2) {
            final int rLng = s1.length();
            final int cLng = s2.length();
            int[][] dp = new int[rLng + 1][cLng + 1];
            for (int i = cLng - 1; i >= 0; i--) {
                dp[rLng][i] = dp[rLng][i + 1] + s2.charAt(i);
            }
            for (int i = rLng - 1; i >= 0; i--) {
                dp[i][cLng] = dp[i + 1][cLng] + s1.charAt(i);
            }
            for (int i = rLng - 1; i >= 0; i--) {
                final char c1 = s1.charAt(i);
                for (int j = cLng - 1; j >= 0; j--) {
                    final char c2 = s2.charAt(j);
                    if (c1 == c2) {
                        dp[i][j] = dp[i + 1][j + 1];
                    } else {
                        dp[i][j] = Math.min(dp[i][j + 1] + c2, dp[i + 1][j] + c1);
                    }
                }
            }
            return dp[0][0];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}