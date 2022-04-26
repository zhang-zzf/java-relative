
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class MaximumSubarraySumAfterOneOperationTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int ans = solution.maxSumAfterOperation(new int[]{2, -1, -4, -3});
        then(ans).isEqualTo(17);
        // fail case 1
        final int ans2 = solution.maxSumAfterOperation(new int[]{1, -1, 1, 1, -1, -1, 1});
        then(ans2).isEqualTo(4);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxSumAfterOperation(int[] nums) {
            int ans = 0;
            int fn = 0, fp = 0;
            for (int num : nums) {
                final int prevMax = Math.max(0, fn);
                fn = prevMax + num;
                fp = Math.max(fp + num, prevMax + num * num);
                ans = Math.max(ans, fp);
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}