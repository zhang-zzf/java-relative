
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class MaxConsecutiveOnesIiiTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int ans = solution.longestOnes(
                new int[]{0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1},
                3);
        then(ans).isEqualTo(10);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int longestOnes(int[] nums, int k) {
            int ans = 0;
            int l = 0, r = 0;
            while (r < nums.length) {
                if (nums[r] == 0) {
                    k -= 1;
                }
                while (k < 0) {
                    if (nums[l] == 0) {
                        k += 1;
                    }
                    l++;
                }
                ans = Math.max(ans, r - l + 1);
                r += 1;
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}