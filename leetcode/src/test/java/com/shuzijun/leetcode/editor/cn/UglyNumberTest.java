
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class UglyNumberTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.isUgly(1)).isTrue();
        then(solution.isUgly(2)).isTrue();
        then(solution.isUgly(14)).isFalse();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public boolean isUgly(int n) {
            int[] primes = new int[]{2, 3, 5};
            while (n > 1) {
                int old = n;
                for (int i = 0; i < primes.length; i++) {
                    if (n % primes[i] == 0) {
                        n = n / primes[i];
                    }
                }
                if (n == old) {
                    break;
                }
            }
            return n == 1;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}