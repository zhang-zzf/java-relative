
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class TargetSumTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.findTargetSumWays(new int[]{1, 1, 1, 1, 1}, 3)).isEqualTo(5);
        then(solution.findTargetSumWays(new int[]{1}, 2)).isEqualTo(0);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int findTargetSumWays(int[] nums, int target) {
            int sum = 0;
            for (int num : nums) {
                sum += num;
            }
            final int sumLng = sum * 2 + 1;
            final int idx = target + sum;
            if (idx >= sumLng || idx < 0) {
                return 0;
            }
            int[] prev = new int[sumLng];
            prev[sum] = 1;
            int[] cur = new int[sumLng];
            for (int num : nums) {
                for (int j = 0; j < sumLng; j++) {
                    cur[j] = ((j + num < sumLng) ? (prev[j + num]) : 0)
                            + ((j - num >= 0) ? (prev[j - num]) : 0);
                }
                int[] tmp = prev;
                prev = cur;
                cur = tmp;
            }
            return prev[idx];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}