package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class HouseRobberTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    then(solution.rob(new int[]{2, 7, 9, 3, 1})).isEqualTo(12);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int rob(int[] nums) {
      int p = 0, q = 0, ans = 0;
      for (int num : nums) {
        ans = Math.max(num + p, q);
        p = q;
        q = ans;
      }
      return ans;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}