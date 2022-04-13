
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class MaximumLengthOfSubarrayWithPositiveProductTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.getMaxLen(new int[]{0, 1, -2, -3, -4})).isEqualTo(3);
        then(solution.getMaxLen(new int[]{-1, -2, -3, 0, 1})).isEqualTo(2);
        then(solution.getMaxLen(new int[]{-16, 0, -5, 2, 2, -13, 11, 8})).isEqualTo(6);
        then(solution.getMaxLen(new int[]{5, -20, -20, -39, -5, 0, 0, 0, 36, -32, 0, -7, -10, -7, 21, 20, -12, -34, 26, 2})).isEqualTo(8);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int getMaxLen(int[] nums) {
            int positiveCnt = 0, negativeCnt = 0, ans = 0;
            for (final int n : nums) {
                if (n == 0) {
                    positiveCnt = negativeCnt = 0;
                    continue;
                }
                if (n > 0) {
                    positiveCnt += 1;
                    negativeCnt = negativeCnt == 0 ? 0 : negativeCnt + 1;
                } else {
                    int newNegative = positiveCnt + 1;
                    positiveCnt = negativeCnt == 0 ? 0 : negativeCnt + 1;
                    negativeCnt = newNegative;
                }
                ans = Math.max(ans, positiveCnt);
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}