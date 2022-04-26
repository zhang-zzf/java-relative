
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class UncrossedLinesTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.maxUncrossedLines(new int[]{2, 5, 1, 2, 5}, new int[]{10, 5, 2, 1, 5, 2})).isEqualTo(3);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxUncrossedLines(int[] nums1, int[] nums2) {
            final int rLng = nums1.length;
            final int cLng = nums2.length;
            int[][] dp = new int[rLng + 1][cLng + 1];
            for (int i = 0; i < rLng; i++) {
                int n1 = nums1[i];
                for (int j = 0; j < cLng; j++) {
                    if (n1 == nums2[j]) {
                        dp[i + 1][j + 1] = dp[i][j] + 1;
                    } else {
                        dp[i + 1][j + 1] = Math.max(dp[i][j + 1], dp[i + 1][j]);
                    }
                }
            }
            return dp[rLng][cLng];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}