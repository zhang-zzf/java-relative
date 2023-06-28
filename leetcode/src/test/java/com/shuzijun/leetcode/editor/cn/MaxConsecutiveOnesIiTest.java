package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class MaxConsecutiveOnesIiTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final int ans = solution.findMaxConsecutiveOnes(new int[]{1, 0, 1, 1, 0});
    then(ans).isEqualTo(4);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int findMaxConsecutiveOnes(int[] nums) {
      int ans = 0;
      int fn = 0, fs = 0;
      for (int num : nums) {
        if (num == 1) {
          fn += 1;
          fs += 1;
        } else if (num == 0) {
          fs = fn + 1;
          fn = 0;
        }
        ans = Math.max(ans, fs);
      }
      return ans;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}