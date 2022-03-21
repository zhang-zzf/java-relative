
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class DeleteAndEarnTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.deleteAndEarn(new int[]{3, 4, 2})).isEqualTo(6);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int deleteAndEarn(int[] nums) {
            int maxVal = nums[0];
            for (int num : nums) {
                maxVal = Math.max(maxVal, num);
            }
            int[] sum = new int[maxVal + 1];
            for (int num : nums) {
                sum[num] += num;
            }
            return rob(sum);
        }

        private int rob(int[] sum) {
            int p = 0, q = sum[0], ans = q;
            for (int i = 1; i < sum.length; i++) {
                ans = Math.max(q, p + sum[i]);
                p = q;
                q = ans;
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}