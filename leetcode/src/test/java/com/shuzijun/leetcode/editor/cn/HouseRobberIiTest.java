package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class HouseRobberIiTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final int rob = solution.rob(new int[]{1, 2, 3, 1});
    then(rob).isEqualTo(4);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int rob(int[] nums) {
      if (nums.length == 1) {
        return nums[0];
      }
      if (nums.length == 2) {
        return Math.max(nums[0], nums[1]);
      }
      // 至少3家
      return Math.max(robRange(nums, 0, nums.length - 1),
          robRange(nums, 1, nums.length));
    }

    /**
     * [start,end)
     */
    private int robRange(int[] nums, int start, int end) {
      // 正常偷
      int p = 0, q = 0, ans = q;
      for (int i = start; i < end; i++) {
        ans = Math.max(nums[i] + p, q);
        p = q;
        q = ans;
      }
      return ans;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}