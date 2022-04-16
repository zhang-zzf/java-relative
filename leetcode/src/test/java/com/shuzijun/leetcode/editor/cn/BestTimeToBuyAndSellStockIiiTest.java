
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class BestTimeToBuyAndSellStockIiiTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.maxProfit(new int[]{3, 3, 5, 0, 0, 3, 1, 4})).isEqualTo(6);
        then(solution.maxProfit(new int[]{6, 1, 3, 2, 4, 7})).isEqualTo(7);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxProfit(int[] prices) {
            int buy1 = -prices[0], sell1 = 0, buy2 = -prices[0], sell2 = 0;
            for (int price : prices) {
                buy1 = Math.max(buy1, -price);
                sell1 = Math.max(sell1, buy1 + price);
                buy2 = Math.max(buy2, sell1 + (-price));
                sell2 = Math.max(sell2, buy2 + price);
            }
            return sell2;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}