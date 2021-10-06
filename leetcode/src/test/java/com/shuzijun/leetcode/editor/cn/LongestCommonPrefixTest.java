//编写一个函数来查找字符串数组中的最长公共前缀。 
//
// 如果不存在公共前缀，返回空字符串 ""。 
//
// 
//
// 示例 1： 
//
// 
//输入：strs = ["flower","flow","flight"]
//输出："fl"
// 
//
// 示例 2： 
//
// 
//输入：strs = ["dog","racecar","car"]
//输出：""
//解释：输入不存在公共前缀。 
//
// 
//
// 提示： 
//
// 
// 1 <= strs.length <= 200 
// 0 <= strs[i].length <= 200 
// strs[i] 仅由小写英文字母组成 
// 
// Related Topics 字符串 👍 1800 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


public class LongestCommonPrefixTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        then(solution.longestCommonPrefix(new String[]{"flower", "flow", "flight"})).isEqualTo("fl");
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public String longestCommonPrefix(String[] strs) {
            int minLength = Integer.MAX_VALUE;
            for (int i = 0; i < strs.length; i++) {
                if (strs[i].length() < minLength) {
                    minLength = strs[i].length();
                }
            }
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < minLength; i++) {
                String str = strAtArray(strs, i);
                if (str == null) {
                    break;
                }
                buf.append(str);
            }
            return buf.toString();
        }

        private String strAtArray(String[] strs, int i) {
            final char c = strs[0].charAt(i);
            for (int j = 0; j < strs.length; j++) {
                if (c != strs[j].charAt(i)) {
                    return null;
                }
            }
            return String.valueOf(c);
        }

    }
//leetcode submit region end(Prohibit modification and deletion)


}