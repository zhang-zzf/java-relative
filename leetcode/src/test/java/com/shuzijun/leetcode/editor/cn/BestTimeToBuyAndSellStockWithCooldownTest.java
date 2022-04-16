
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
            // f1 没有股票且在冷冻期， f2 没有股票且不在冷冻期
            int f0 = -prices[0], f1 = 0, f2 = 0;
            for (int price : prices) {
                int curF0 = Math.max(f0, f2 + (-price));
                // 昨天有股票，今天把股票卖了
                int curF1 = f0 + price;
                f2 = Math.max(f2, f1);
                f0 = curF0;
                f1 = curF1;
            }
            return Math.max(f1, f2);
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}