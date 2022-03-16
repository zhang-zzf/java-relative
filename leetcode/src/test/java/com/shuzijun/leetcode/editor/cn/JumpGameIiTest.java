
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class JumpGameIiTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.jump(new int[]{2, 3, 1, 1, 4})).isEqualTo(2);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int jump(int[] nums) {
            int[] dp = new int[nums.length];
            for (int i = 1; i < nums.length; i++) {
                int minStep = 1;
                for (int j = 0; j < i; j++) {
                    if (j + nums[j] >= i) {
                        minStep = dp[j] + 1;
                        break;
                    }
                }
                dp[i] = minStep;
            }
            return dp[nums.length - 1];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}