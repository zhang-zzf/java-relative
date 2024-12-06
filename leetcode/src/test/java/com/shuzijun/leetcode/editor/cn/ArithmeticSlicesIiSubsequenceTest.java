package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;


class ArithmeticSlicesIiSubsequenceTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int number = solution.numberOfArithmeticSlices(new int[]{2, 4, 6, 8, 10});
        then(number).isEqualTo(7);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int numberOfArithmeticSlices(int[] nums) {
            int ans = 0;
            final int n = nums.length;
            Map<Long, Integer>[] dp = new Map[n];
            for (int i = 0; i < n; i++) {
                dp[i] = new HashMap<>();
                for (int j = 0; j < i; j++) {
                    long d = (long) nums[i] - nums[j];
                    final Integer tmp = dp[j].getOrDefault(d, 0);
                    dp[i].put(d, dp[i].getOrDefault(d, 0) + tmp + 1);
                    ans += tmp;
                }
            }
            return ans;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}