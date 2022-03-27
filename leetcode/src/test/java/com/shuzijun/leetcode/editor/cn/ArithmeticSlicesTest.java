
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class ArithmeticSlicesTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int number = solution.numberOfArithmeticSlices(new int[]{3, -1, -5, -9});
        then(number).isEqualTo(3);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int numberOfArithmeticSlices(int[] nums) {
            int ans = 0;
            if (nums.length < 2) {
                return ans;
            }
            int curNum = 0, subVal = nums[1] - nums[0];
            for (int i = 2; i < nums.length; i++) {
                final int sub = nums[i] - nums[i - 1];
                curNum = (sub == subVal) ? curNum + 1 : 0;
                subVal = sub;
                ans += curNum;
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}