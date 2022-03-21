
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class HouseRobberTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.rob(new int[]{2, 7, 9, 3, 1})).isEqualTo(12);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int rob(int[] nums) {
            int p = 0, q = nums[0], ans = q;
            for (int i = 1; i < nums.length; i++) {
                ans = Math.max(nums[i] + p, q);
                p = q;
                q = ans;
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}