
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class MaximumSubarrayTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int maxSubArray = solution.maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4});
        then(maxSubArray).isEqualTo(6);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxSubArray(int[] nums) {
            int prev = nums[0], max = prev;
            for (int i = 1; i < nums.length; i++) {
                prev = Math.max(prev + nums[i], nums[i]);
                max = Math.max(max, prev);
            }
            return max;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}