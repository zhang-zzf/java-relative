package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


class MaximumLengthOfRepeatedSubarrayTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {

    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int findLength(int[] nums1, int[] nums2) {
            int ans = 0;
            int[][] dp = new int[nums1.length + 1][nums2.length + 1];
            for (int i = 0; i < nums1.length; i++) {
                for (int j = 0; j < nums2.length; j++) {
                    dp[i + 1][j + 1] = (nums1[i] == nums2[j]) ? dp[i][j] + 1 : 0;
                    ans = Math.max(ans, dp[i + 1][j + 1]);
                }
            }
            return ans;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}