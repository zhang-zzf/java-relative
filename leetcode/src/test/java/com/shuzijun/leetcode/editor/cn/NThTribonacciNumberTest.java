
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class NThTribonacciNumberTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int tribonacci = solution.tribonacci(25);
        then(tribonacci).isEqualTo(1389537);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int tribonacci(int n) {
            if (n == 0) {
                return 0;
            }
            if (n == 1) {
                return 1;
            }
            if (n == 2) {
                return 1;
            }
            int p = 0, q = 1, r = 1, ans = 2;
            for (int i = 3; i <= n; i++) {
                ans = p + q + r;
                p = q;
                q = r;
                r = ans;
            }
            return ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}