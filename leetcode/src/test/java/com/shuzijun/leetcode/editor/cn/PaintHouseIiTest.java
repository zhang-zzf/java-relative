package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class PaintHouseIiTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final int minCostII = solution.minCostII(new int[][]{
        new int[]{1, 5, 3},
        new int[]{2, 9, 4}
    });
    then(minCostII).isEqualTo(5);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int minCostII(int[][] costs) {
      int min = 0;
      int secMin = 0;
      int minIdx = 0;
      for (int[] cost : costs) {
        int nowMin = Integer.MAX_VALUE;
        int nowSecMin = Integer.MAX_VALUE;
        int nowMinIdx = 0;
        for (int i = 0; i < cost.length; i++) {
          int val = (minIdx == i ? secMin : min) + cost[i];
          if (val < nowMin) {
            nowSecMin = nowMin;
            nowMin = val;
            nowMinIdx = i;
          } else if (val < nowSecMin) {
            nowSecMin = val;
          }
        }
        min = nowMin;
        minIdx = nowMinIdx;
        secMin = nowSecMin;
      }
      return min;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}