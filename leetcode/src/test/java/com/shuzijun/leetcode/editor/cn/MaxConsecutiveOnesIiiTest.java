
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class MaxConsecutiveOnesIiiTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int ans = solution.longestOnes(
                new int[]{0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1},
                3);
        then(ans).isEqualTo(10);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int longestOnes(int[] nums, int k) {
            int ans = 0;
            // k + 1 的状态表示用掉 0 次 / 1 次 / k 次机会后的最长1的个数
            int[] dp = new int[k + 1];
            for (int num : nums) {
                if (num == 0) {
                    for (int i = k; i > 0; i--) {
                        dp[i] = dp[i - 1] + 1;
                    }
                    dp[0] = 0;
                } else if (num == 1) {
                    for (int i = 0; i <= k; i++) {
                        dp[i] = dp[i] + 1;
                    }
                }
                ans = Math.max(ans, dp[k]);
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}