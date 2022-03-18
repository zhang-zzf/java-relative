
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
            int curMin = nums[0], curMax = nums[0], maxProduct = nums[0];
            for (int i = 1; i < nums.length; i++) {
                final int m1 = nums[i] * curMin;
                final int m2 = nums[i] * curMax;
                curMin = Math.min(nums[i], Math.min(m1, m2));
                curMax = Math.max(nums[i], Math.max(m1, m2));
                maxProduct = Math.max(maxProduct, curMax);
            }
            return maxProduct;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}