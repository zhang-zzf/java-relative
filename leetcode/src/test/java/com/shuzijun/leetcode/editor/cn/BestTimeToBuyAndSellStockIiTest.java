
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
            int ans = 0;
            for (int i = 1; i < prices.length; i++) {
                final int profit = prices[i] - prices[i - 1];
                if (profit > 0) {
                    ans += profit;
                }
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}