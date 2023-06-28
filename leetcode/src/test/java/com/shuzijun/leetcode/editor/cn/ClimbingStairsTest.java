package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class ClimbingStairsTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    then(solution.climbStairs(2)).isEqualTo(2);
    then(solution.climbStairs(3)).isEqualTo(3);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int climbStairs(int n) {
      if (n == 1) {
        return 1;
      }
      if (n == 2) {
        return 2;
      }
      int n2 = 1, n1 = 2;
      int ans = 0;
      for (int i = 3; i <= n; i++) {
        ans = n2 + n1;
        n2 = n1;
        n1 = ans;
      }
      return ans;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}