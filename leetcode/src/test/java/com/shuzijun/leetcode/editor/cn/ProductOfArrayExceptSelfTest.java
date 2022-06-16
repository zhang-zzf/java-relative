
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class ProductOfArrayExceptSelfTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.productExceptSelf(new int[]{-1, 1, 0, -3, 3})).containsExactly(0, 0, 9, 0, 0);
        then(solution.productExceptSelf(new int[]{1, 2, 3, 4})).containsExactly(24, 12, 8, 6);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int[] productExceptSelf(int[] nums) {
            int[] dp = new int[nums.length];
            // 前缀积
            dp[0] = 1;
            for (int i = 1; i < nums.length; i++) {
                dp[i] = dp[i - 1] * nums[i - 1];
            }
            int suffix = 1;
            for (int i = nums.length - 2; i >= 0; i--) {
                suffix *= nums[i + 1];
                dp[i] = dp[i] * suffix;
            }
            return dp;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}