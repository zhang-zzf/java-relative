
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;


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
                int max = 1;
                for (int j = 0; j < i; j++) {
                    if (nums[i] % nums[j] == 0) {
                        max = Math.max(max, dp[j] + 1);
                    }
                }
                dp[i] = max;
                if (max > dp[maxIdx]) {
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