
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class PartitionEqualSubsetSumTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.canPartition(new int[]{1, 5, 11, 5})).isTrue();
        then(solution.canPartition(new int[]{1, 2, 3, 5})).isFalse();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public boolean canPartition(int[] nums) {
            int[] sumAndMax = calcSumAndMaxValue(nums);
            int W = sumAndMax[0] / 2;
            if (sumAndMax[0] % 2 != 0 || sumAndMax[1] > W) {
                return false;
            }
            boolean[] dp = new boolean[W + 1];
            dp[0] = true;
            for (int num : nums) {
                for (int i = W; i >= num; i--) {
                    dp[i] = dp[i] || dp[i - num];
                }
            }
            return dp[W];
        }

        private int[] calcSumAndMaxValue(int[] nums) {
            final int[] ans = new int[2];
            for (int num : nums) {
                ans[0] += num;
                ans[1] = Math.max(ans[1], num);
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}