package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


class MaximumProductSubarrayTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    then(solution.maxProduct(new int[]{2, 3, -2, 4})).isEqualTo(6);
    then(solution.maxProduct(new int[]{-2, 0, -1})).isEqualTo(0);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public int maxProduct(int[] nums) {
      int curMin = 1, curMax = 1, maxProduct = nums[0];
      for (int num : nums) {
        final int m1 = num * curMin;
        final int m2 = num * curMax;
        curMin = Math.min(num, Math.min(m1, m2));
        curMax = Math.max(num, Math.max(m1, m2));
        maxProduct = Math.max(maxProduct, curMax);
      }
      return maxProduct;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}