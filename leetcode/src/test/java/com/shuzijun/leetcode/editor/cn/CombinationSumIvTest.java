package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


class CombinationSumIvTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {

    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int combinationSum4(int[] nums, int target) {
            int[] dp = new int[target + 1];
            dp[0] = 1;
            for (int i = 1; i <= target; i++) {
                for (int num : nums) {
                    dp[i] += (i - num < 0 ? 0 : dp[i - num]);
                }
            }
            return dp[target];
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}