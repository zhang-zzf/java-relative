package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


class LongestContinuousIncreasingSubsequenceTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {

  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int findLengthOfLCIS(int[] nums) {
      int ans = 1;
      int dp = 1;
      for (int i = 1; i < nums.length; i++) {
        dp = (nums[i] > nums[i - 1]) ? dp + 1 : 1;
        ans = Math.max(ans, dp);
      }
      return ans;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}