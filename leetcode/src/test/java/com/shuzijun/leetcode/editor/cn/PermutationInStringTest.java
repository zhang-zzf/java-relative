//给你两个字符串 s1 和 s2 ，写一个函数来判断 s2 是否包含 s1 的排列。如果是，返回 true ；否则，返回 false 。 
//
// 换句话说，s1 的排列之一是 s2 的 子串 。 
//
// 
//
// 示例 1： 
//
// 
//输入：s1 = "ab" s2 = "eidbaooo"
//输出：true
//解释：s2 包含 s1 的排列之一 ("ba").
// 
//
// 示例 2： 
//
// 
//输入：s1= "ab" s2 = "eidboaoo"
//输出：false
// 
//
// 
//
// 提示： 
//
// 
// 1 <= s1.length, s2.length <= 10⁴ 
// s1 和 s2 仅包含小写字母 
// 
//
// Related Topics 哈希表 双指针 字符串 滑动窗口 
// 👍 817 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class PermutationInStringTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.checkInclusion("ab", "eidbaooo")).isTrue();
        then(solution.checkInclusion("ab", "eidboaoo")).isFalse();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public boolean checkInclusion(String s1, String s2) {
            int[] chars = new int[128];
            for (int i = 0; i < chars.length; i++) {
                chars[i] = -1;
            }
            for (int i = 0; i < s1.length(); i++) {
                char c = s1.charAt(i);
                chars[c] = (chars[c] == -1) ? 1 : chars[c] + 1;
            }
            for (int l = 0, r = 0; r < s2.length(); r++) {
                char c = s2.charAt(r);
                if (chars[c] > 0) {
                    chars[c] -= 1;
                } else {
                    while (l <= r) {
                        char lc = s2.charAt(l++);
                        if (lc == c) {
                            break;
                        }
                        chars[lc] += 1;
                    }
                }
                if (r - l + 1 == s1.length()) {
                    return true;
                }
            }
            return false;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}