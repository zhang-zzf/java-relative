
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class ProductOfArrayExceptSelfTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int[] ints = solution.productExceptSelf(new int[]{-1, 1, 0, -3, 3});
        then(ints).containsExactly(0, 0, 9, 0, 0);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int[] productExceptSelf(int[] nums) {
            final int lng = nums.length;
            int[] left = new int[lng];
            int[] right = new int[lng];
            for (int i = 0; i < lng; i++) {
                left[i] = i == 0 ? 1 : left[i - 1] * nums[i - 1];
            }
            for (int i = lng - 1; i >= 0; i--) {
                right[i] = (i + 1) == lng ? 1 : right[i + 1] * nums[i + 1];
            }
            int[] ans = new int[lng];
            for (int i = 0; i < lng; i++) {
                ans[i] = left[i] * right[i];
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}