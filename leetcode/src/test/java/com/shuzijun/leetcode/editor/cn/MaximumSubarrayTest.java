package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class MaximumSubarrayTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int maxSubArray = solution.maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4});
        then(maxSubArray).isEqualTo(6);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxSubArray(int[] nums) {
            int prev = Integer.MIN_VALUE, max = nums[0];
            for (int num : nums) {
                prev = Math.max(prev, 0) + num;
                max = Math.max(max, prev);
            }
            return max;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}