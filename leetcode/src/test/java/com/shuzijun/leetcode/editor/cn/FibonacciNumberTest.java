// 斐波那契数，通常用 F(n) 表示，形成的序列称为 斐波那契数列 。该数列由 0 和 1 开始，后面的每一项数字都是前面两项数字的和。也就是：
//
// 
// F(0) = 0，F(1) = 1
// F(n) = F(n - 1) + F(n - 2)，其中 n > 1
// 
//
// 给你 n ，请计算 F(n) 。 
//
// 
//
// 示例 1： 
//
// 
// 输入：2
// 输出：1
// 解释：F(2) = F(1) + F(0) = 1 + 0 = 1
// 
//
// 示例 2： 
//
// 
// 输入：3
// 输出：2
// 解释：F(3) = F(2) + F(1) = 1 + 1 = 2
// 
//
// 示例 3： 
//
// 
// 输入：4
// 输出：3
// 解释：F(4) = F(3) + F(2) = 2 + 1 = 3
// 
//
// 
//
// 提示： 
//
// 
// 0 <= n <= 30 
// 
// Related Topics 递归 记忆化搜索 数学 动态规划 👍 371 👎 0


package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;


public class FibonacciNumberTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int fib = solution.fib(5);
        then(fib).isEqualTo(5);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int fib(int n) {
            if (n < 2) {
                return n;
            }
            int p = 0, q = 1, r = 1;
            for (int i = 2; i <= n; i++) {
                r = p + q;
                p = q;
                q = r;
            }
            return r;
        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}