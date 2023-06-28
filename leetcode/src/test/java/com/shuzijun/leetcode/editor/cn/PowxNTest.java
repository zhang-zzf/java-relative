//实现 pow(x, n) ，即计算 x 的 n 次幂函数（即，xⁿ ）。 
//
// 
//
// 示例 1： 
//
// 
//输入：x = 2.00000, n = 10
//输出：1024.00000
// 
//
// 示例 2： 
//
// 
//输入：x = 2.10000, n = 3
//输出：9.26100
// 
//
// 示例 3： 
//
// 
//输入：x = 2.00000, n = -2
//输出：0.25000
//解释：2-2 = 1/22 = 1/4 = 0.25
// 
//
// 
//
// 提示： 
//
// 
// -100.0 < x < 100.0 
// -231 <= n <= 231-1 
// -104 <= xⁿ <= 104 
// 
// Related Topics 递归 数学 👍 878 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;


@Slf4j
public class PowxNTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {
    then(solution.myPow(2.0, 10)).isCloseTo(1024, Offset.offset(0.1));
    then(solution.myPow(2.1, 3)).isCloseTo(9.261, Offset.offset(0.1));
    then(solution.myPow(2.0, -2)).isCloseTo(0.25, Offset.offset(0.1));
    then(solution.myPow(2.0, 13)).isCloseTo(8192, Offset.offset(0.1));
  }


  @Test
  void testIntegerMin() {
    final int intMin = -2147483648;
    log.info("intMin: {}", intMin);
    log.info("--2147483648: {}", -intMin);
    log.info("-((long)-2147483648): {}", -((long) intMin));
    log.info("Math.abs(-2147483648): {}", Math.abs(intMin));
    log.info("Integer.MIN_VALUE: {}", Integer.MIN_VALUE);
    log.info("Integer.MAX_VALUE: {}", Integer.MAX_VALUE);
  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public double myPow(double x, int n) {
      if (n == 0) {
        return 1.0;
      }
      double ans = 1.0;
      // int n = -2147483648; -> (-n == -2147483648) is true
      // 边界值
      long N = Math.abs((long) n);
      double x_contribute = x;
      while (N > 0) {
        if (N % 2 == 1) {
          ans *= x_contribute;
        }
        x_contribute *= x_contribute;
        N /= 2;
      }
      return n < 0 ? 1.0 / ans : ans;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}