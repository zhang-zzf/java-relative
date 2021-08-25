//给你一个整数 x ，如果 x 是一个回文整数，返回 true ；否则，返回 false 。 
//
// 回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。例如，121 是回文，而 123 不是。 
//
// 
//
// 示例 1： 
//
// 
//输入：x = 121
//输出：true
// 
//
// 示例 2： 
//
// 
//输入：x = -121
//输出：false
//解释：从左向右读, 为 -121 。 从右向左读, 为 121- 。因此它不是一个回文数。
// 
//
// 示例 3： 
//
// 
//输入：x = 10
//输出：false
//解释：从右向左读, 为 01 。因此它不是一个回文数。
// 
//
// 示例 4： 
//
// 
//输入：x = -101
//输出：false
// 
//
// 
//
// 提示： 
//
// 
// -2³¹ <= x <= 2³¹ - 1 
// 
//
// 
//
// 进阶：你能不将整数转为字符串来解决这个问题吗？ 
// Related Topics 数学 👍 1594 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class PalindromeNumberTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.isPalindrome(1)).isTrue();
        then(solution.isPalindrome(9)).isTrue();
        then(solution.isPalindrome(10)).isFalse();
        then(solution.isPalindrome(11)).isTrue();
        then(solution.isPalindrome(121)).isTrue();
        then(solution.isPalindrome(-121)).isFalse();
        then(solution.isPalindrome(120)).isFalse();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean isPalindrome(int x) {
            if (x < 0) {
                return false;
            } else if (x < 10) {
                return true;
            } else if (x % 10 == 0) {
                return false;
            }
            int reverse = 0;
            while (true) {
                reverse = reverse * 10 + x % 10;
                x /= 10;
                if (reverse == x) {
                    break;
                } else if (reverse > x) {
                    reverse = reverse / 10;
                    break;
                }

            }
            return reverse == x;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)


}