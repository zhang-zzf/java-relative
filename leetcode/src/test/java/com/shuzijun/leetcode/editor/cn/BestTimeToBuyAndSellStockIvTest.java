
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.BDDAssertions.then;


class BestTimeToBuyAndSellStockIvTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.maxProfit(2, new int[]{2, 4, 1})).isEqualTo(2);
        then(solution.maxProfit(2, new int[]{3, 2, 6, 5, 0, 3})).isEqualTo(7);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxProfit(int k, int[] prices) {
            if (k == 0) {
                return 0;
            }
            int[] buy = new int[k];
            int[] sell = new int[k];
            Arrays.fill(buy, Integer.MIN_VALUE);
            for (int i = 0; i < prices.length; i++) {
                for (int j = 0; j < k; j++) {
                    buy[j] = Math.max(buy[j], (j == 0 ? 0 : sell[j - 1]) + (-prices[i]));
                    sell[j] = Math.max(sell[j], buy[j] + prices[i]);
                }
            }
            return sell[k - 1];
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}