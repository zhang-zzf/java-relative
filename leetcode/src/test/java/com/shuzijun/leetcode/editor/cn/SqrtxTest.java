
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class SqrtxTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.mySqrt(8)).isEqualTo(2);
        then(solution.mySqrt(2147395599)).isEqualTo(46339);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int mySqrt(int x) {
            int ans = 0;
            // 值域 [0...X] 内的整数
            int left = 0, right = x;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                if (((long) mid) * mid <= x) {
                    ans = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}