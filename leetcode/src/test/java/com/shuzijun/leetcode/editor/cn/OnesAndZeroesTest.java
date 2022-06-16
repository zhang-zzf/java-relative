
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class OnesAndZeroesTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int ans = solution.findMaxForm(new String[]{"10", "0001", "111001", "1", "0"}, 5, 3);
        then(ans).isEqualTo(4);
        // fail case 1
        then(solution.findMaxForm(new String[]{"10", "1", "0"}, 1, 1)).isEqualTo(2);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int findMaxForm(String[] strs, int m, int n) {
            int[][] dp = new int[m + 1][n + 1];
            for (String str : strs) {
                int _1Cnt = 0;
                int _0Cnt = 0;
                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) == '0') {
                        _0Cnt += 1;
                    } else {
                        _1Cnt += 1;
                    }
                }
                for (int i = m; i >= _0Cnt; i--) {
                    for (int j = n; j >= _1Cnt; j--) {
                        dp[i][j] = Math.max(dp[i][j], dp[i - _0Cnt][j - _1Cnt] + 1);
                    }
                }
            }
            return dp[m][n];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}