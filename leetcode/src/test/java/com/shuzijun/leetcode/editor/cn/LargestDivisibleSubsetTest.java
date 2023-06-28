package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;


class LargestDivisibleSubsetTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    final List<Integer> ans = solution.largestDivisibleSubset(new int[]{2, 4, 7, 8, 9, 12, 16, 20});
    then(ans).contains(16, 8, 4, 2);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public List<Integer> largestDivisibleSubset(int[] nums) {
      Arrays.sort(nums);
      final int lng = nums.length;
      int[] dp = new int[lng];
      int maxIdx = 0;
      for (int i = 0; i < lng; i++) {
        for (int j = 0; j < i; j++) {
          if (nums[i] % nums[j] == 0) {
            dp[i] = Math.max(dp[i], dp[j] + 1);
          }
        }
        if (dp[i] > dp[maxIdx]) {
          maxIdx = i;
        }
      }
      List<Integer> ans = new ArrayList<>();
      int maxValue = nums[maxIdx];
      int maxLng = dp[maxIdx];
      for (int i = maxIdx; i >= 0; i--) {
        if (dp[i] == maxLng && maxValue % nums[i] == 0) {
          ans.add(nums[i]);
          // careful
          maxValue = nums[i];
          maxLng -= 1;
        }
      }
      return ans;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}