package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


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
        for (int prime : primes) {
          if (n % prime == 0) {
            n = n / prime;
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