
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
            final int lng = nums.length;
            int[] ans = new int[lng];
            for (int i = 0; i < lng; i++) {
                ans[i] = i == 0 ? 1 : ans[i - 1] * nums[i - 1];
            }
            int right = 1;
            for (int i = lng - 1; i >= 0; i--) {
                right *= (i + 1) == lng ? 1 : nums[i + 1];
                ans[i] *= right;
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}