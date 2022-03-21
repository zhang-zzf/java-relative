
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class BestTimeToBuyAndSellStockTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.maxProfit(new int[]{7, 1, 5, 3, 6, 4})).isEqualTo(5);
        then(solution.maxProfit(new int[]{7, 6, 4, 3, 1})).isEqualTo(0);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxProfit(int[] prices) {
            int min = prices[0], max = min, ans = 0;
            for (int i = 0; i < prices.length; i++) {
                if (prices[i] < min) {
                    max = min = prices[i];
                } else if (prices[i] > max) {
                    max = prices[i];
                    ans = Math.max(ans, max - min);
                }
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}