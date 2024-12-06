package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class MinCostClimbingStairsTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int cost = solution.minCostClimbingStairs(new int[]{10, 15, 20});
        then(cost).isEqualTo(15);
        then(solution.minCostClimbingStairs(new int[]{1, 100, 1, 1, 1, 100, 1, 1, 100, 1})).isEqualTo(
            6);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int minCostClimbingStairs(int[] cost) {
            final int length = cost.length;
            int fn_2 = 0, fn_1 = 0;
            int ans = 0;
            for (int i = 2; i <= length; i++) {
                ans = Math.min(fn_1 + cost[i - 1], fn_2 + cost[i - 2]);
                fn_2 = fn_1;
                fn_1 = ans;
            }
            return ans;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}