
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
            final int lng = nums.length;
            if (lng < 2) {
                return false;
            }
            int sum = 0;
            int maxVal = nums[0];
            for (int num : nums) {
                sum += num;
                maxVal = Math.max(maxVal, num);
            }
            int half = sum / 2;
            if (sum % 2 != 0 || maxVal > half) {
                return false;
            }
            // 转换成背包问题：在 nums 存在子序列，其和为 half
            int[][] dp = new int[half + 1][lng + 1];
            // dp[0][0..n] = 1;
            Arrays.fill(dp[0], 1);
            for (int subSum = 1; subSum <= half; subSum++) {
                for (int i = 0; i < lng; i++) {
                    dp[subSum][i + 1] = ((dp[subSum][i])
                            | (subSum - nums[i] >= 0 ? dp[subSum - nums[i]][i] : 0));

                }
            }
            return dp[half][lng] == 1;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}