package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


class MaxConsecutiveOnesTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {

    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int findMaxConsecutiveOnes(int[] nums) {
            int ans = 0;
            int fn = 0;
            for (int num : nums) {
                if (num == 0) {
                    fn = 0;
                }
                else if (num == 1) {
                    fn += 1;
                }
                ans = Math.max(ans, fn);
            }
            return ans;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}