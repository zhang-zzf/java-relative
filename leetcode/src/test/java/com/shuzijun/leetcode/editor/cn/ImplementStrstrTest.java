//实现 strStr() 函数。 
//
// 给你两个字符串 haystack 和 needle ，请你在 haystack 字符串中找出 needle 字符串出现的第一个位置（下标从 0 开始）。如
//果不存在，则返回 -1 。 
//
// 
//
// 说明： 
//
// 当 needle 是空字符串时，我们应当返回什么值呢？这是一个在面试中很好的问题。 
//
// 对于本题而言，当 needle 是空字符串时我们应当返回 0 。这与 C 语言的 strstr() 以及 Java 的 indexOf() 定义相符。 
//
// 
//
// 示例 1： 
//
// 
//输入：haystack = "hello", needle = "ll"
//输出：2
// 
//
// 示例 2： 
//
// 
//输入：haystack = "aaaaa", needle = "bba"
//输出：-1
// 
//
// 示例 3： 
//
// 
//输入：haystack = "", needle = ""
//输出：0
// 
//
// 
//
// 提示： 
//
// 
// 0 <= haystack.length, needle.length <= 5 * 10⁴ 
// haystack 和 needle 仅由小写英文字符组成 
// 
// Related Topics 双指针 字符串 字符串匹配 👍 1062 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class ImplementStrstrTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.next("abcdabce".toCharArray())).containsExactly(-1, 0, 0, 0, 0, 1, 2, 3);
        then(solution.next("abcdabde".toCharArray())).containsExactly(-1, 0, 0, 0, 0, 1, 2, 0);
        then(solution.strStr("aabaaabaaac", "aabaaac")).isEqualTo(4);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int strStr(String haystack, String needle) {
            final char[] chars = haystack.toCharArray();
            final char[] needleArray = needle.toCharArray();
            if (needleArray.length == 0) {
                return 0;
            }
            final int[] next = next(needleArray);
            for (int i = 0; i + needleArray.length <= chars.length; ) {
                for (int j = 0; j < needleArray.length; j++) {
                    if (chars[i + j] != needleArray[j]) {
                        i += j - next[j];
                        break;
                    }
                    if (j == needleArray.length - 1) {
                        return i;
                    }
                }
            }
            return -1;
        }

        public int[] next(char[] str) {
            int[] next = new int[str.length];
            if (str.length == 0) {
                return next;
            }
            next[0] = -1;
            if (str.length == 1) {
                return next;
            }
            next[1] = 0;
            if (str.length == 2) {
                return next;
            }
            for (int i = 1; i < str.length - 1; i++) {
                int k = next[i];
                if (str[i] == str[k]) {
                    next[i + 1] = k + 1;
                    continue;
                }
                for (k = next[i]; k >= 0; k = next[k]) {
                    if (str[i] == str[k]) {
                        next[i + 1] = k + 1;
                        break;
                    }
                }
            }
            return next;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}