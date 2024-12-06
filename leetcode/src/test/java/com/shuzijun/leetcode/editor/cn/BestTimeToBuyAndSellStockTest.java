package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class BestTimeToBuyAndSellStockTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.maxProfit(new int[]{7, 1, 5, 3, 6, 4})).isEqualTo(5);
        then(solution.maxProfit(new int[]{7, 6, 4, 3, 1})).isEqualTo(0);
        // fail case 1
        then(solution.maxProfit(new int[]{2, 4, 1})).isEqualTo(2);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxProfit(int[] prices) {
            int buy = -prices[0], sell = 0;
            for (int price : prices) {
                buy = Math.max(buy, -price);
                sell = Math.max(sell, buy + price);
            }
            return sell;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}