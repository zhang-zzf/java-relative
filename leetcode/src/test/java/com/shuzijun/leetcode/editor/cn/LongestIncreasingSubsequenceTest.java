
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class LongestIncreasingSubsequenceTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int lengthOfLIS = solution.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18});
        then(lengthOfLIS).isEqualTo(4);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int lengthOfLIS(int[] nums) {
            // dp[n] 表示以 nums[n] 结尾的最长递增子序列的长度
            int[] dp = new int[nums.length];
            int ans = 1;
            dp[0] = 1;
            for (int i = 1; i < nums.length; i++) {
                dp[i] = 1;
                for (int j = 0; j < i; j++) {
                    if (nums[j] < nums[i]) {
                        dp[i] = Math.max(dp[i], dp[j] + 1);
                    }
                }
                ans = Math.max(ans, dp[i]);
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}