
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class ShortestWayToFormStringTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.shortestWay("xyz", "xzyxz")).isEqualTo(3);
        then(solution.shortestWay("aaaa", "aaaaaaaaaaaaa")).isEqualTo(4);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int shortestWay(String source, String target) {
            int ans = 0;
            Integer[][] dp = new Integer[source.length()][26];
            for (int i = 0; i < source.length(); i++) {
                for (int j = i; j < source.length(); j++) {
                    final int sourceIdx = source.charAt(j) - 'a';
                    if (dp[i][sourceIdx] == null) {
                        dp[i][sourceIdx] = j;
                    }
                }
            }
            for (int i = 0; i < target.length(); ) {
                Integer idx = -1;
                int maxLng = 0;
                while (idx + 1 < source.length() && (i + maxLng) < target.length()) {
                    char c = target.charAt(i + maxLng);
                    if ((idx = dp[idx + 1][c - 'a']) == null) {
                        break;
                    }
                    maxLng += 1;
                }
                if (maxLng == 0) {
                    ans = -1;
                    break;
                }
                i += maxLng;
                ans += 1;
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}