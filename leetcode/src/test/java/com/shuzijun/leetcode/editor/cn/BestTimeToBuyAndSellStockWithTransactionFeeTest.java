package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class BestTimeToBuyAndSellStockWithTransactionFeeTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final int profit = solution.maxProfit(new int[]{1, 3, 2, 8, 4, 9}, 2);
    then(profit).isEqualTo(8);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int maxProfit(int[] prices, int fee) {
      int buy = -prices[0] - fee, sell = 0;
      for (int price : prices) {
        // 买股票时付手续费
        buy = Math.max(buy, sell + (-price) - fee);
        sell = Math.max(sell, buy + price);
      }
      return sell;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}