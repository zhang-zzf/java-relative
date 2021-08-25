//给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。 
//
// 
//
// 示例 1: 
//
// 
//输入: s = "abcabcbb"
//输出: 3 
//解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
// 
//
// 示例 2: 
//
// 
//输入: s = "bbbbb"
//输出: 1
//解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
// 
//
// 示例 3: 
//
// 
//输入: s = "pwwkew"
//输出: 3
//解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
//     请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
// 
//
// 示例 4: 
//
// 
//输入: s = ""
//输出: 0
// 
//
// 
//
// 提示： 
//
// 
// 0 <= s.length <= 5 * 10⁴ 
// s 由英文字母、数字、符号和空格组成 
// 
// Related Topics 哈希表 字符串 滑动窗口 👍 5981 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class LongestSubstringWithoutRepeatingCharactersTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.lengthOfLongestSubstring("")).isEqualTo(0);
        then(solution.lengthOfLongestSubstring("pwwkew")).isEqualTo(3);
        then(solution.lengthOfLongestSubstring("abcabcbb")).isEqualTo(3);
        then(solution.lengthOfLongestSubstring("bbbb")).isEqualTo(1);
        then(solution.lengthOfLongestSubstring(" ")).isEqualTo(1);
        then(solution.lengthOfLongestSubstring("au")).isEqualTo(2);
        then(solution.lengthOfLongestSubstring("dvdf")).isEqualTo(3);
        then(solution.lengthOfLongestSubstring("abba")).isEqualTo(2);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int lengthOfLongestSubstring(String s) {
            int[] lastIndex = new int[128];
            for (int i = 0; i < lastIndex.length; i++) {
                lastIndex[i] = -1;
            }
            int maxLength = 0;
            for (int r = 0, l = 0; r < s.length(); r++) {
                char c = s.charAt(r);
                l = Math.max(l, lastIndex[c] + 1);
                maxLength = Math.max(maxLength, r - l + 1);
                lastIndex[c] = r;
            }
            return maxLength;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)


}