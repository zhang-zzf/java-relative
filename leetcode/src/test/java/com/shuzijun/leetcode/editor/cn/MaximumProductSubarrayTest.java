
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class MaximumProductSubarrayTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.maxProduct(new int[]{2, 3, -2, 4})).isEqualTo(6);
        then(solution.maxProduct(new int[]{-2, 0, -1})).isEqualTo(0);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxProduct(int[] nums) {
            int[] curMax = new int[]{nums[0], nums[0]};
            int multiplyMax = nums[0];
            for (int i = 1; i < nums.length; i++) {
                final int m1 = curMax[0] * nums[i];
                final int m2 = curMax[1] * nums[i];
                if (nums[i] < 0 && m1 >= 0 && m2 >= 0) {
                    curMax[0] = nums[i];
                } else {
                    curMax[0] = Math.min(m1, m2);
                }
                curMax[1] = Math.max(nums[i], Math.max(m1, m2));
                multiplyMax = Math.max(multiplyMax, curMax[1]);
            }
            return multiplyMax;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}