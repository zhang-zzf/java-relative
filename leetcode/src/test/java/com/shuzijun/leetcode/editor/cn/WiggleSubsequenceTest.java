
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class WiggleSubsequenceTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int ans = solution.wiggleMaxLength(new int[]{1, 7, 4, 9, 2, 5});
        then(ans).isEqualTo(6);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int wiggleMaxLength(int[] nums) {
            int positive = 1, negative = 1;
            for (int i = 1; i < nums.length; i++) {
                int diff = nums[i] - nums[i - 1];
                if (diff > 0) {
                    positive = negative + 1;
                } else if (diff < 0) {
                    negative = positive + 1;
                }
            }
            return Math.max(positive, negative);
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}