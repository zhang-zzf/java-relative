package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class JumpGameTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    then(solution.canJump(new int[]{2, 3, 1, 1, 4})).isTrue();
    then(solution.canJump(new int[]{3, 2, 1, 0, 4})).isFalse();
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public boolean canJump(int[] nums) {
      int rightMax = 0;
      for (int i = 0; i < nums.length; i++) {
        if (i <= rightMax) {
          rightMax = Math.max(rightMax, i + nums[i]);
          if (rightMax >= nums.length - 1) {
            return true;
          }
        }
      }
      return false;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}