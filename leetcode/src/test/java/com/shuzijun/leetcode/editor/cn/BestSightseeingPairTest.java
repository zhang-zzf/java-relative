
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class BestSightseeingPairTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int pair = solution.maxScoreSightseeingPair(new int[]{8, 1, 5, 2, 6});
        then(pair).isEqualTo(11);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxScoreSightseeingPair(int[] values) {
            int max = values[0] + 0, ans = 0;
            for (int i = 1; i < values.length; i++) {
                ans = Math.max(ans, max + values[i] - i);
                max = Math.max(max, values[i] + i);
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}