
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


class BestTimeToBuyAndSellStockIiTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {

    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxProfit(int[] prices) {
            int buy = -prices[0], sell = 0;
            for (int i = 0; i < prices.length; i++) {
                buy = Math.max(buy, sell + (-prices[i]));
                sell = Math.max(sell, buy + prices[i]);
            }
            return sell;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}