package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


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

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int findNumberOfLIS(int[] nums) {
            int[] lngDp = new int[nums.length];
            int[] cntDp = new int[nums.length];
            int maxLng = 0;
            for (int i = 0; i < nums.length; i++) {
                lngDp[i] = 1;
                for (int j = 0; j < i; j++) {
                    if (nums[j] < nums[i]) {
                        lngDp[i] = Math.max(lngDp[i], lngDp[j] + 1);
                    }
                }
                maxLng = Math.max(maxLng, lngDp[i]);
                // 统计以 nums[i] 结尾， 长度为 lngDp[i] 的子序列的数量
                for (int j = 0; j < i; j++) {
                    if (nums[j] < nums[i] && lngDp[j] == lngDp[i] - 1) {
                        cntDp[i] += cntDp[j];
                    }
                }
                cntDp[i] = Math.max(cntDp[i], 1);
            }
            int ans = 0;
            for (int i = 0; i < lngDp.length; i++) {
                if (lngDp[i] == maxLng) {
                    ans += cntDp[i];
                }
            }
            return ans;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}