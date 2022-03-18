
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class MaximumSumCircularSubarrayTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.maxSubarraySumCircular(new int[]{5, -3, 5})).isEqualTo(10);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxSubarraySumCircular(int[] nums) {
            int curMax = nums[0], sumMax = curMax,
                    curMin = nums[0], sumMin = curMin;
            int totalSum = nums[0];
            for (int i = 1; i < nums.length; i++) {
                curMax = nums[i] + Math.max(0, curMax);
                sumMax = Math.max(sumMax, curMax);
                curMin = nums[i] + Math.min(curMin, 0);
                sumMin = Math.min(sumMin, curMin);
                totalSum += nums[i];
            }
            sumMax = sumMax < 0 ? sumMax : Math.max(sumMax, totalSum - sumMin);
            return sumMax;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}