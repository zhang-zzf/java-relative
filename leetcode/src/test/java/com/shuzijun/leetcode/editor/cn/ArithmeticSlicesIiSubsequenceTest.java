
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;


class ArithmeticSlicesIiSubsequenceTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int number = solution.numberOfArithmeticSlices(new int[]{2, 4, 6, 8, 10});
        then(number).isEqualTo(7);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int numberOfArithmeticSlices(int[] nums) {
            int ans = 0;
            if (nums.length < 2) {
                return ans;
            }
            Map<Long, Integer>[] dp = new Map[nums.length];
            for (int i = 0; i < nums.length; i++) {
                dp[i] = new HashMap<>();
            }
            for (int i = 1; i < nums.length; i++) {
                for (int j = i - 1; j >= 0; j--) {
                    long sub = 1L * nums[i] - nums[j];
                    final Integer cnt = dp[j].getOrDefault(sub, 0);
                    ans += cnt;
                    dp[i].put(sub, dp[i].getOrDefault(sub, 0) + cnt + 1);
                }
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}