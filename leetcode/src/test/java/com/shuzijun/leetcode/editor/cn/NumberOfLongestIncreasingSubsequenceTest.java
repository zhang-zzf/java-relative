
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class NumberOfLongestIncreasingSubsequenceTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.findNumberOfLIS(new int[]{1, 3, 5, 4, 7})).isEqualTo(2);
        then(solution.findNumberOfLIS(new int[]{2, 2, 2, 2, 2})).isEqualTo(5);
        // fail case 1
        // fail reason: 计算 cnt 时要严格考虑 nums[j] < nums[i]
        then(solution.findNumberOfLIS(new int[]{1, 2, 4, 3, 5, 4, 7, 2})).isEqualTo(3);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int findNumberOfLIS(int[] nums) {
            final int lng = nums.length;
            int[] dp = new int[lng];
            int finalMaxLng = 1;
            int[] cnt = new int[lng];
            // 已 nums[i] 结尾的数据统计
            // 统计 maxLng / maxCnt
            for (int i = 0; i < lng; i++) {
                int maxLng = 1;
                for (int j = 0; j < i; j++) {
                    if (nums[j] < nums[i]) {
                        maxLng = Math.max(maxLng, dp[j] + 1);
                    }
                }
                dp[i] = maxLng;
                finalMaxLng = Math.max(finalMaxLng, dp[i]);
                // 统计 cnt
                int maxCnt = 0;
                for (int j = 0; j < i; j++) {
                    if (dp[j] == maxLng - 1 && nums[j] < nums[i]) {
                        maxCnt += cnt[j];
                    }
                }
                cnt[i] = maxCnt == 0 ? 1 : maxCnt;
            }
            int finalCnt = 0;
            for (int i = 0; i < lng; i++) {
                if (dp[i] == finalMaxLng) {
                    finalCnt += cnt[i];
                }
            }
            return finalCnt;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}