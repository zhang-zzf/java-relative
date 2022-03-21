
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class BestTimeToBuyAndSellStockWithCooldownTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.maxProfit(new int[]{1, 2, 3, 0, 2})).isEqualTo(3);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxProfit(int[] prices) {
            int f0 = -prices[0], f1 = 0, f2 = 0;
            for (int i = 1; i < prices.length; i++) {
                int newf0 = Math.max(f0, f2 - prices[i]);
                int newf1 = f0 + prices[i];
                f2 = Math.max(f2, f1);
                f0 = newf0;
                f1 = newf1;
            }
            return Math.max(f1, f2);
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}