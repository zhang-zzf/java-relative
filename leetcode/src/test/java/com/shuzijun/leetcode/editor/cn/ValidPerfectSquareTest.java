//给定一个 正整数 num ，编写一个函数，如果 num 是一个完全平方数，则返回 true ，否则返回 false 。
//
// 进阶：不要 使用任何内置的库函数，如 sqrt 。
//
//
//
// 示例 1：
//
//
//输入：num = 16
//输出：true
//
//
// 示例 2：
//
//
//输入：num = 14
//输出：false
//
//
//
//
// 提示：
//
//
// 1 <= num <= 2^31 - 1
//
// Related Topics 数学 二分查找 👍 349 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;


public class ValidPerfectSquareTest {

  final Solution solution = new Solution();

  @Test
  void givenNormal_when_thenSuccess() {

  }

  //leetcode submit region begin(Prohibit modification and deletion)
  class Solution {

    public boolean isPerfectSquare(int num) {
      // 值域[0, num]
      int left = 0, right = num;
      while (left <= right) {
        int mid = left + ((right - left) >> 1);
        // 核心点：必须按先把 mid 转换为 long 再 * mid
        final long x2 = (long) mid * mid;
        if (x2 == num) {
          return true;
        } else if (x2 < num) {
          left = mid + 1;
        } else {
          right = mid - 1;
        }
      }
      return false;
    }

  }
//leetcode submit region end(Prohibit modification and deletion)


}